package com.salestock.api.event.order;

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
@JsonDeserialize(builder = OrderExpired.OrderExpiredBuilder.class)
public class OrderExpired extends BaseOrderObject{

	@Builder
	public OrderExpired(OrderId orderId) {
		super(orderId);
	}
	
	@JsonPOJOBuilder(withPrefix = "")
    public static final class OrderExpiredBuilder {
    }
}
