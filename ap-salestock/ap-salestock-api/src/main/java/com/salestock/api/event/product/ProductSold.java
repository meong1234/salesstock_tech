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
@JsonDeserialize(builder = ProductSold.ProductSoldBuilder.class)
@Value
public class ProductSold extends BaseProductObject {
	
	private int qty;
	
	private Money price;
	
	private Money totalPrice;

	@Builder
	public ProductSold(ProductId productId, int qty, Money price, Money totalPrice) {
		super(productId);
		this.qty = qty;
		this.price = price;
		this.totalPrice = totalPrice;
	}
	
	@JsonPOJOBuilder(withPrefix = "")
    public static final class ProductSoldBuilder {
    }

}
