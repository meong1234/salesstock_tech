package com.salestock.api.event.product;

import org.axonframework.common.Assert;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.salestock.api.BaseProductObject;
import com.salestock.api.identifier.OrderId;
import com.salestock.api.identifier.ProductId;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonDeserialize(builder = ProductError.ProductErrorBuilder.class)
@Value
public class ProductError extends BaseProductObject{

	private OrderId orderId;
	
	private String message;

	@Builder
	public ProductError(ProductId productId, OrderId orderId, String message) {
		super(productId);
		
		Assert.notNull(orderId, "orderId can't null");
		this.message = message;
		this.orderId = orderId;
	}
	
	@JsonPOJOBuilder(withPrefix = "")
    public static final class ProductErrorBuilder {
    }
	
}
