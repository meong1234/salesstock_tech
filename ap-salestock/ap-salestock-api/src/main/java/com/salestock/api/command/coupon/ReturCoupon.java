package com.salestock.api.command.coupon;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.salestock.api.BaseCouponObject;
import com.salestock.api.identifier.CouponId;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonDeserialize(builder = ReturCoupon.ReturCouponBuilder.class)
@Value
public class ReturCoupon extends BaseCouponObject {

	@Builder
	public ReturCoupon(CouponId couponId) {
		super(couponId);
	}
	
	@JsonPOJOBuilder(withPrefix = "")
    public static final class ReturCouponBuilder {
    }

}
