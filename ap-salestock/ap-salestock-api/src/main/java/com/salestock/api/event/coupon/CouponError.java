package com.salestock.api.event.coupon;

import org.axonframework.common.Assert;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.salestock.api.BaseCouponObject;
import com.salestock.api.event.product.ProductError;
import com.salestock.api.identifier.CouponId;
import com.salestock.api.identifier.OrderId;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonDeserialize(builder = ProductError.ProductErrorBuilder.class)
@Value
public class CouponError extends BaseCouponObject{

	private OrderId orderId;
	
	private String message;

	@Builder
	public CouponError(CouponId couponId, OrderId orderId, String message) {
		super(couponId);
		
		Assert.notNull(orderId, "orderId can't null");
		this.message = message;
		this.orderId = orderId;
	}
	
	@JsonPOJOBuilder(withPrefix = "")
    public static final class CouponErrorBuilder {
    }

}
