package com.salestock.api.command.coupon;

import org.axonframework.common.Assert;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.salestock.api.BaseCouponObject;
import com.salestock.api.identifier.CouponId;
import com.salestock.api.identifier.OrderId;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonDeserialize(builder = DeductCoupon.DeductCouponBuilder.class)
@Value
public class DeductCoupon extends BaseCouponObject {
	
	private OrderId orderId;

	@Builder
	public DeductCoupon(CouponId couponId, OrderId orderId) {
		super(couponId);
		Assert.notNull(orderId, "orderId can't null");
		this.orderId = orderId;
	}
	
	@JsonPOJOBuilder(withPrefix = "")
    public static final class DeductCouponBuilder {
    }
}
