package com.salestock.domain.write.order;

import java.util.List;
import java.util.Optional;

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.common.Assert;
import org.axonframework.eventhandling.EventTemplate;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ap.config.axon.util.AbstractCommandHandler;
import com.ap.misc.util.ValidatorUtil;
import com.salestock.api.command.order.SubmitOrder;
import com.salestock.api.dto.order.OrderDetail;
import com.salestock.domain.query.coupon.CouponDictionary;
import com.salestock.domain.query.coupon.CouponService;
import com.salestock.domain.query.product.ProductService;

@Component
public class OrderCommandHandler extends AbstractCommandHandler<OrderAggregate>{
	
	private Repository<OrderAggregate> repository;

	private EventTemplate eventTemplate;
	
	private CouponService couponService;
	
	private ProductService productService;

	public OrderCommandHandler() {
	}
	
	@Autowired
	public OrderCommandHandler(Repository<OrderAggregate> repository, EventTemplate eventTemplate,
			CouponService couponService, ProductService productService) {
		
		Assert.notNull(repository, "repository is null");
		Assert.notNull(eventTemplate, "eventTemplate is null");
		Assert.notNull(couponService, "couponService is null");
		Assert.notNull(productService, "productService is null");
		
		this.repository = repository;
		this.eventTemplate = eventTemplate;
		this.couponService = couponService;
		this.productService = productService;
	}

	private OrderAggregate aggLoad(Object aggregateIdentifier) {
		try {
			OrderAggregate agg = repository.load(aggregateIdentifier);
			return agg;
		} catch (AggregateNotFoundException e) {
			throw new OrderException("order not found for id" + aggregateIdentifier.toString());
		}
	}
	
	@CommandHandler
	public void onSubmitOrder(SubmitOrder cmd) {
		
		//validate product
		cmd.getDetail().forEach( e -> {			
			if (!productService.isValidProduct(e.getProductId().toString(), e.getQty())) {
				throw new OrderException("one product qty not enought, with productid " +e.getProductId().toString() );
			}
		});
		
		//check for submited coupon
		Optional<CouponDictionary> coupon = Optional.empty();
		if (ValidatorUtil.isPresent(cmd.getCouponId())) {
			
			coupon= couponService.find(cmd.getCouponId().toString());
			if (coupon.isPresent() && (!coupon.get().isValid())) {
				throw new OrderException("coupon qty not enought for couponid" + cmd.getCouponId().getIdentifier());
			}
		}
		
		//mapping our orderDetailDto to orderDetail
		List<OrderDetail> orderDetail = productService.convertDetailsDto(cmd.getDetail());
		
		OrderAggregate agg = new OrderAggregate(cmd, coupon, orderDetail);
		repository.add(agg);
	}

	@Override
	public void setRepository(Repository<OrderAggregate> repository) {
		this.repository = repository;		
	}

	@Override
	public void setEventTemplate(EventTemplate template) {
		this.eventTemplate = template;			
	}

}
