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
@JsonDeserialize(builder = SellProductFailed.SellProductFailedBuilder.class)
@Value
public class SellProductFailed extends BaseProductObject{

	private String message;

	@Builder
	public SellProductFailed(ProductId productId, String message) {
		super(productId);
		this.message = message;
	}
	
	@JsonPOJOBuilder(withPrefix = "")
    public static final class SellProductFailedBuilder {
    }
	
}
