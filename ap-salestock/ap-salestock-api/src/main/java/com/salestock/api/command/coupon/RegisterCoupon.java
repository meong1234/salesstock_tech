package com.salestock.api.command.coupon;

import org.axonframework.common.Assert;

import com.ap.misc.util.ValidatorUtil;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.salestock.api.dto.coupon.BaseCoupon;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@EqualsAndHashCode
@ToString
@JsonDeserialize(builder = RegisterCoupon.RegisterCouponBuilder.class)
@Value
public class RegisterCoupon {

	private int startingQty;
	
	private BaseCoupon info;

	@Builder
	public RegisterCoupon(int startingQty, BaseCoupon info) {

		Assert.isTrue(ValidatorUtil.isPresent(startingQty), "coupon qty not present");
		Assert.notNull(info, "coupon info can't null");
		
		this.startingQty = startingQty;
		this.info = info;
	}
	
	
	@JsonPOJOBuilder(withPrefix = "")
    public static final class RegisterCouponBuilder {
    }
}
