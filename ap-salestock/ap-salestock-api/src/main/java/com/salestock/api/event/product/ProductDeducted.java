package com.salestock.api.event.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.salestock.api.BaseProductObject;
import com.salestock.api.identifier.OrderId;
import com.salestock.api.identifier.ProductId;
import com.salestock.shared.Money;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonDeserialize(builder = ProductDeducted.ProductDeductedBuilder.class)
@Value
public class ProductDeducted extends BaseProductObject {
	
	private OrderId orderId;
	
	private int qty;
	
	private Money price;
	
	private Money totalPrice;
	
	//for indepotem 
	private int endQty;

	@Builder
	public ProductDeducted(ProductId productId, OrderId orderId, int qty, Money price, Money totalPrice, int endQty) {
		super(productId);
		this.orderId = orderId;
		this.qty = qty;
		this.price = price;
		this.totalPrice = totalPrice;
		this.endQty = endQty;
	}
	
	@JsonPOJOBuilder(withPrefix = "")
    public static final class ProductDeductedBuilder {
    }

}
