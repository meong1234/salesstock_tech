package com.salestock.api.event.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.salestock.api.BaseProductObject;
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

	private String message;

	@Builder
	public ProductError(ProductId productId, String message) {
		super(productId);
		this.message = message;
	}
	
	@JsonPOJOBuilder(withPrefix = "")
    public static final class ProductErrorBuilder {
    }
	
}
