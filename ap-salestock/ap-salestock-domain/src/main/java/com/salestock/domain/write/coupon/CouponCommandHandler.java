package com.salestock.domain.write.coupon;

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.common.Assert;
import org.axonframework.eventhandling.EventTemplate;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ap.config.axon.util.AbstractCommandHandler;
import com.salestock.api.command.coupon.DeductCoupon;
import com.salestock.api.command.coupon.RegisterCoupon;
import com.salestock.api.command.coupon.ReturCoupon;
import com.salestock.api.event.coupon.CouponError;
import com.salestock.domain.write.product.ProductException;

@Component
public class CouponCommandHandler extends AbstractCommandHandler<CouponAggregate>{

	private Repository<CouponAggregate> repository;

	private EventTemplate eventTemplate;

	public CouponCommandHandler() {
	}

	@Autowired
	public CouponCommandHandler(Repository<CouponAggregate> repository, EventTemplate eventTemplate) {

		Assert.notNull(repository, "repository is null");
		Assert.notNull(eventTemplate, "eventTemplate is null");

		this.repository = repository;
		this.eventTemplate = eventTemplate;
	}
	
	private CouponAggregate aggLoad(Object aggregateIdentifier) {
		try {
			CouponAggregate agg = repository.load(aggregateIdentifier);
			return agg;
		} catch (AggregateNotFoundException e) {
			throw new CouponException("coupon not found for id" + aggregateIdentifier.toString());
		}
	}
	
	@CommandHandler
	public void onDeductProduct(DeductCoupon cmd) {
		try {
			CouponAggregate agg = this.aggLoad(cmd.getCouponId());
			agg.deductCoupon(cmd.getOrderId());
		} catch (ProductException | IllegalArgumentException e) {
			System.out.println(e.getMessage());
			eventTemplate.publishEvent(new CouponError(cmd.getCouponId(), cmd.getOrderId(), e.getMessage()));
		}
	}
	
	@CommandHandler
	public void onRegisterCoupon(RegisterCoupon cmd) {
		CouponAggregate agg = new CouponAggregate(cmd);
		repository.add(agg);
	}
	
	@CommandHandler
	public void onReturCoupon(ReturCoupon cmd) {
		try {
			CouponAggregate agg = this.aggLoad(cmd.getCouponId());
			agg.returnCoupon();
		} catch (ProductException | IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void setRepository(Repository<CouponAggregate> repository) {
		this.repository = repository;		
	}

	@Override
	public void setEventTemplate(EventTemplate template) {
		this.eventTemplate = template;		
	}
}
