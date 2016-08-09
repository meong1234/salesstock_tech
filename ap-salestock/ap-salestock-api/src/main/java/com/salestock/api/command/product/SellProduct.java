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
@JsonDeserialize(builder = SellProduct.SellProductBuilder.class)
@Value
public class SellProduct extends BaseProductObject {
	
	private int qty;

	@Builder
	public SellProduct(ProductId productId, int qty) {
		super(productId);
		this.qty = qty;
	}
	
	@JsonPOJOBuilder(withPrefix = "")
    public static final class SellProductBuilder {
    }
}
