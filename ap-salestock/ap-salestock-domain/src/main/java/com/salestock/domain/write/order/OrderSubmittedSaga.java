package com.salestock.domain.write.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.axonframework.common.annotation.MetaData;
import org.axonframework.eventhandling.scheduling.EventScheduler;
import org.axonframework.eventhandling.scheduling.ScheduleToken;
import org.axonframework.saga.annotation.AbstractAnnotatedSaga;
import org.axonframework.saga.annotation.EndSaga;
import org.axonframework.saga.annotation.SagaEventHandler;
import org.axonframework.saga.annotation.StartSaga;
import org.joda.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;

import com.ap.config.axon.util.MyCommandGateway;
import com.ap.config.axon.util.UserMetaData;
import com.ap.config.axon.util.correlationevent.CorrelationToken;
import com.ap.domain.base.SessionDto;
import com.salestock.api.EnumStatus;
import com.salestock.api.command.coupon.DeductCoupon;
import com.salestock.api.command.coupon.ReturCoupon;
import com.salestock.api.command.order.OrderFail;
import com.salestock.api.command.product.DeductProduct;
import com.salestock.api.command.product.ReturProduct;
import com.salestock.api.dto.order.OrderDetail;
import com.salestock.api.event.coupon.CouponDeducted;
import com.salestock.api.event.coupon.CouponError;
import com.salestock.api.event.order.OrderExpired;
import com.salestock.api.event.order.OrderSubmitted;
import com.salestock.api.event.product.ProductDeducted;
import com.salestock.api.event.product.ProductError;
import com.salestock.api.identifier.CouponId;
import com.salestock.api.identifier.OrderId;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderSubmittedSaga extends AbstractAnnotatedSaga {

	@Autowired
	@Setter
	private transient EventScheduler eventScheduler;

	@Autowired
	@Setter
	private transient MyCommandGateway commandGateway;

	private ScheduleToken expiredToken;

	private SessionDto sessionData;

	private CorrelationToken correlationToken;
	
	private Boolean markCouponOk = false;
	private Boolean markCouponDone = false;
	
	private Boolean markProductOk = false;
	private Boolean markProductDone = false;
	
	private CouponId couponId;

	private List<OrderDetail> orderDetails = new ArrayList<>();

	private Map<String, EnumStatus> productDeductStatus = new HashMap<String, EnumStatus>();

	private void clearToken() {
		if (expiredToken != null) {
			eventScheduler.cancelSchedule(expiredToken);
		}
	}

	@StartSaga
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(OrderSubmitted event, @MetaData(UserMetaData.SESSIONDATA) SessionDto sessionData,
			@MetaData(CorrelationToken.KEY) CorrelationToken correlationToken) {

		log.debug("OrderSubmittedSaga started on event {}", event);

		clearToken();
		this.sessionData = sessionData;
		this.correlationToken = correlationToken;
		this.orderDetails.addAll(event.getDetail());
		this.couponId = event.getCouponId();
		
		//mark as complete on coupon
		if (this.couponId != null) {
			this.markCouponDone = true;
			this.markCouponOk = true;
		}

		// TODO make sure when we deduct product, on

		// deduct product
		event.getDetail().forEach(e -> {

			commandGateway.send(new DeductProduct(e.getProductId(), event.getOrderId(), e.getQty()),
					this.correlationToken, this.sessionData);
		});

		// deduct coupon
		commandGateway.send(new DeductCoupon(event.getCouponId(), event.getOrderId()), this.correlationToken,
				this.sessionData);

		// TODO atm use 2minute for wait deduct product and deduct coupon
		// success
		this.expiredToken = eventScheduler.schedule(Duration.standardMinutes(2), new OrderExpired(event.getOrderId()));
	}
	
	private List<String> getSuccedProduct() {
		List<String> succeedDeduct = new ArrayList<>();
		
		//filter success :p
		for (Map.Entry<String, EnumStatus> entry : productDeductStatus.entrySet()) {
			if (entry.getValue().equals(EnumStatus.Success)) {
				succeedDeduct.add(entry.getKey());
			}
		}
		
		return succeedDeduct;
	}
	
	private void returSuccedProduct() {
		getSuccedProduct()
		.forEach(e -> {
			
			OrderDetail detail = this
					.orderDetails
					.stream()
					.filter(p -> p.getProductId().getIdentifier().equals(e))
					.findAny().get();
			
			commandGateway.send(new ReturProduct(detail.getProductId(), detail.getQty()), this.correlationToken,
					this.sessionData);
		});
	}
	
	private void completed(OrderId orderId) {
		
		if (!(this.markCouponDone && this.markProductDone)) {
			return;
		}
		
		if (this.markProductOk && this.markCouponOk) {
			
		} else {
			commandGateway.send(new OrderFail(orderId, "fail on deduct"), this.correlationToken,
					this.sessionData);
		}
		
		//send returcoupon if fail
		if ((this.couponId != null) && (!this.markCouponOk)) {
			commandGateway.send(new ReturCoupon(this.couponId), this.correlationToken,
					this.sessionData);
		}
		
		//send retur product is fail
		if (!this.markProductOk) {
			returSuccedProduct();
		}
		
		clearToken();
		end();
	}

	public void completedProduct(OrderId orderId) {
		
		//is all deducted product done ?
		if (productDeductStatus.size() == orderDetails.size()) {
			
			this.markProductDone = true;
			
			if (productDeductStatus.size() == getSuccedProduct().size()) {
				this.markProductOk = true;
			};
		}
		
		completed(orderId);
	}

	@SagaEventHandler(associationProperty = "orderId")
	public void handle(ProductDeducted event) {

		log.debug("orderSubmitedSaga recieved ProductDeducted {}", event);

		productDeductStatus.put(event.getOrderId().toString(), EnumStatus.Success);

		completedProduct(event.getOrderId());
	}

	@SagaEventHandler(associationProperty = "orderId")
	public void handle(final ProductError event) {

		log.debug("orderSubmitedSaga recieved ProductError {}", event);

		productDeductStatus.put(event.getOrderId().toString(), EnumStatus.Fail);

		completedProduct(event.getOrderId());
	}
	
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(final CouponDeducted event) {
		log.debug("orderSubmitedSaga recieved CouponDeducted {}", event);
		
		this.markCouponDone = true;
		this.markCouponOk = true;
		completed(event.getOrderId());
	}

	@EndSaga
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(final CouponError event) {
		
		log.debug("orderSubmitedSaga recieved CouponDeducted {}", event);
		
		this.markCouponDone = true;

		returSuccedProduct();

		commandGateway.send(new OrderFail(event.getOrderId(), "Coupon Deduct error"), this.correlationToken,
				this.sessionData);

		clearToken();
	}

	@EndSaga
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(final OrderExpired event) {

		commandGateway.send(new OrderFail(event.getOrderId(), "expired order"), this.correlationToken,
				this.sessionData);

		// TODO retur succeed deduct coupon and product

		clearToken();
	}
}
