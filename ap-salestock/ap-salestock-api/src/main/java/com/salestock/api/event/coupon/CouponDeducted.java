package com.salestock.api.event.coupon;

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
@JsonDeserialize(builder = CouponDeducted.CouponDeductedBuilder.class)
@Value
public class CouponDeducted extends BaseCouponObject{
	
	private OrderId orderId;
	
	//for indepotem 
	private int endQty;

	@Builder
	public CouponDeducted(CouponId couponId, OrderId orderId, int endQty) {
		super(couponId);
		this.orderId = orderId;
		this.endQty = endQty;
	}
	
	@JsonPOJOBuilder(withPrefix = "")
    public static final class CouponDeductedBuilder {
    }

}
