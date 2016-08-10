package com.salestock.api.event.coupon;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.salestock.api.BaseCouponObject;
import com.salestock.api.dto.coupon.BaseCoupon;
import com.salestock.api.identifier.CouponId;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonDeserialize(builder = CouponRegistered.CouponRegisteredBuilder.class)
@Value
public class CouponRegistered extends BaseCouponObject {
	
	private int startingQty;
	
	private BaseCoupon info;

	@Builder
	public CouponRegistered(CouponId couponId, int startingQty, BaseCoupon info) {
		super(couponId);
		this.startingQty = startingQty;
		this.info = info;
	}
	
	@JsonPOJOBuilder(withPrefix = "")
    public static final class CouponRegisteredBuilder {
    }

}
