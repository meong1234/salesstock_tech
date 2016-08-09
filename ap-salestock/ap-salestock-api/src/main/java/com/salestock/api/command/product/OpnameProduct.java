package com.salestock.api.command.product;

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
@JsonDeserialize(builder = OpnameProduct.OpnameProductBuilder.class)
@Value
public class OpnameProduct extends BaseProductObject {

	private int newQty;

	private Money newPrice;

	@Builder
	public OpnameProduct(ProductId productId, int newQty, Money newPrice) {
		super(productId);
		this.newQty = newQty;
		this.newPrice = newPrice;
	}
	
	@JsonPOJOBuilder(withPrefix = "")
    public static final class OpnameProductBuilder {
    }
}
