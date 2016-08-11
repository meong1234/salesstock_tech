package com.salestock.api.dto.order;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.salestock.api.BaseProductObject;
import com.salestock.api.identifier.ProductId;
import com.salestock.shared.Money;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonDeserialize(builder = OrderDetail.OrderDetailBuilder.class)
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class OrderDetail extends BaseProductObject {
	
	private String productName;
	
	private int qty;
	
	private Money price;
	
	@JsonProperty(access = Access.READ_ONLY)
	private Money totalPrice;
	
	@Builder
	public OrderDetail(ProductId productId, String productName, int qty, Money price) {
		super(productId);
		this.productName = productName;
		this.qty = qty;
		this.price = price;
		this.totalPrice = price.multiply(new BigDecimal(qty));
	}

	@JsonPOJOBuilder(withPrefix = "")
    public static final class OrderDetailBuilder {
    }
}
