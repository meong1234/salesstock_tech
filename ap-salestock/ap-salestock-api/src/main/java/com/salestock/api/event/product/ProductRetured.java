package com.salestock.api.event.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.salestock.api.BaseProductObject;
import com.salestock.api.command.product.ReturProduct;
import com.salestock.api.identifier.ProductId;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonDeserialize(builder = ProductRetured.ProductReturedBuilder.class)
@Value
public class ProductRetured extends BaseProductObject {
	
	private int returQty;
	
	//for indepotem 
	private int endQty;

	@Builder
	public ProductRetured(ProductId productId, int returQty, int endQty) {
		super(productId);
		
		this.returQty = returQty;
		this.endQty = endQty;
	}
	
	@JsonPOJOBuilder(withPrefix = "")
    public static final class ProductReturedBuilder {
    }
	
	

}
