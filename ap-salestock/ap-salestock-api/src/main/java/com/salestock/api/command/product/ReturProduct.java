package com.salestock.api.command.product;

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
@JsonDeserialize(builder = ReturProduct.ReturProductBuilder.class)
@Value
public class ReturProduct extends BaseProductObject {

	private int returQty;

	@Builder
	public ReturProduct(ProductId productId, int returQty) {
		super(productId);
		
		this.returQty = returQty;
	}
	
	@JsonPOJOBuilder(withPrefix = "")
    public static final class ReturProductBuilder {
    }
}
