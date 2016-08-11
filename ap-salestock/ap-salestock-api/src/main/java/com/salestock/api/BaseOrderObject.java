package com.salestock.api;

import org.axonframework.common.Assert;

import com.salestock.api.identifier.OrderId;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Getter
public abstract class BaseOrderObject {

	private OrderId orderId;

	public BaseOrderObject(OrderId orderId) {
		
		Assert.notNull(orderId, "orderId must not be null");
		this.orderId = orderId;
	}
}

