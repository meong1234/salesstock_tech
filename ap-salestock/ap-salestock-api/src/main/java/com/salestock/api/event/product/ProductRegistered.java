package com.salestock.api.event.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.salestock.api.BaseProductObject;
import com.salestock.api.identifier.ProductId;
import com.salestock.shared.Money;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonDeserialize(builder = ProductRegistered.ProductRegisteredBuilder.class)
@Value
public class ProductRegistered extends BaseProductObject{
	
	private String productName;
	
	private int startingQty;
	
	private Money startingPrice;

	@Builder
	public ProductRegistered(ProductId productId, String productName, int startingQty, Money startingPrice) {
		super(productId);
		this.productName = productName;
		this.startingQty = startingQty;
		this.startingPrice = startingPrice;
	}

	@JsonPOJOBuilder(withPrefix = "")
    public static final class ProductRegisteredBuilder {
    }
}
