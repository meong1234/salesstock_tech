package com.salestock.api;

import org.axonframework.common.Assert;

import com.ap.misc.util.ValidatorUtil;
import com.salestock.api.identifier.ProductId;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Getter
public abstract class BaseProductObject {

	private ProductId productId;

	public BaseProductObject(ProductId productId) {
		
		Assert.notNull(productId, "productId must not be null");
		this.productId = productId;
	}
	
	
}
