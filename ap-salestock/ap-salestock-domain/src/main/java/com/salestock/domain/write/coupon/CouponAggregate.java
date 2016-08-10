package com.salestock.domain.write.coupon;

import org.axonframework.common.Assert;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;
import org.axonframework.eventsourcing.annotation.EventSourcingHandler;

import com.salestock.api.command.coupon.RegisterCoupon;
import com.salestock.api.dto.coupon.BaseCoupon;
import com.salestock.api.event.coupon.CouponDeducted;
import com.salestock.api.event.coupon.CouponRegistered;
import com.salestock.api.identifier.CouponId;
import com.salestock.api.identifier.OrderId;

public class CouponAggregate extends AbstractAnnotatedAggregateRoot<CouponId>{
	
	@AggregateIdentifier
	private CouponId couponId;

	private int qty;
	
	private BaseCoupon couponInfo;

	CouponAggregate() {
	}
	
	/**
	 * Create / register new coupon.
	 */
	public CouponAggregate(RegisterCoupon cmd) {
		apply(CouponRegistered
				.builder()
				.couponId(new CouponId())
				.startingQty(cmd.getStartingQty())
				.info(cmd.getInfo())
				.build()
			);
	}
	
	/**
	 * deduct coupon.
	 * 
	 * @return
	 */
	public void deductCoupon(OrderId orderId) {		
		Assert.isTrue(this.qty > 0, "not enaough qty for this coupon "+this.couponId.toString());
		Assert.isTrue(!this.couponInfo.isExpired(), "this coupon "+this.couponId.toString()+" already expired");

		apply(
			CouponDeducted
			.builder()
			.couponId(this.couponId)
			.endQty(this.qty-1)
			.orderId(orderId)
			.build()
		);
	}
	
	@EventSourcingHandler
	private void onCouponRegistered(CouponRegistered event) {
		this.couponId = event.getCouponId();
		this.qty = event.getStartingQty();
		this.couponInfo = event.getInfo();
	}
	
	@EventSourcingHandler
	private void onCouponDeducted(CouponDeducted event) {
		this.qty--;
	}

}
