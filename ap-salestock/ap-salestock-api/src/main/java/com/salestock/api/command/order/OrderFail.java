package com.salestock.api.command.order;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.salestock.api.BaseOrderObject;
import com.salestock.api.identifier.OrderId;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@ToString(callSuper=true)
@EqualsAndHashCode(callSuper=true)
@Value
@JsonDeserialize(builder = OrderFail.OrderFailBuilder.class)
public class OrderFail extends BaseOrderObject{
	
	private String message;
	
	@Builder
	public OrderFail(OrderId orderId, String message) {
		super(orderId);
		this.message = message;
	}

	@JsonPOJOBuilder(withPrefix = "")
    public static final class OrderFailBuilder {
    }

}
