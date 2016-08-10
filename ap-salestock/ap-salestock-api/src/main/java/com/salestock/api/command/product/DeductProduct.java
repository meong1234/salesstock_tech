package com.salestock.api.command.product;

import org.axonframework.common.Assert;

import com.ap.misc.util.ValidatorUtil;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.salestock.api.BaseProductObject;
import com.salestock.api.identifier.OrderId;
import com.salestock.api.identifier.ProductId;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonDeserialize(builder = DeductProduct.DeductProductBuilder.class)
@Value
public class DeductProduct extends BaseProductObject {
	
	private OrderId orderId;
	
	private int qty;

	@Builder
	public DeductProduct(ProductId productId, OrderId orderId, int qty) {
		super(productId);
		
		Assert.isTrue(ValidatorUtil.isPresent(qty), "qty must be present");
		Assert.notNull(orderId, "orderId can't null");
		
		this.orderId = orderId;
		this.qty = qty;
	}
	
	@JsonPOJOBuilder(withPrefix = "")
    public static final class DeductProductBuilder {
    }
}
