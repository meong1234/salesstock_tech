package com.salestock.api;

import org.axonframework.common.Assert;

import com.salestock.api.identifier.CouponId;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Getter
public abstract class BaseCouponObject {

	private CouponId couponId;

	public BaseCouponObject(CouponId couponId) {
		
		Assert.notNull(couponId, "couponId must not be null");
		this.couponId = couponId;
	}
}
