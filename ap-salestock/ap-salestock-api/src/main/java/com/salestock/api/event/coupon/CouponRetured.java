package com.salestock.api.event.coupon;

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
@JsonDeserialize(builder = CouponRetured.CouponReturedBuilder.class)
@Value
public class CouponRetured extends BaseCouponObject{
	
	//for indepotem 
	private int endQty;

	@Builder
	public CouponRetured(CouponId couponId, int endQty) {
		super(couponId);
		this.endQty = endQty;
	}
	
	@JsonPOJOBuilder(withPrefix = "")
    public static final class CouponReturedBuilder {
    }

}
