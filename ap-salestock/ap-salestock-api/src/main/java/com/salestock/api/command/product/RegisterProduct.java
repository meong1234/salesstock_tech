package com.salestock.api.command.product;

import org.axonframework.common.Assert;

import com.ap.misc.util.ValidatorUtil;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.salestock.shared.Money;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@EqualsAndHashCode
@ToString
@JsonDeserialize(builder = RegisterProduct.RegisterProductBuilder.class)
@Value
public class RegisterProduct {
	
	private String productName;
	
	private int startingQty;
	
	private Money startingPrice;

	@Builder
	public RegisterProduct(String productName, int startingQty, Money startingPrice) {

		Assert.isTrue(ValidatorUtil.isPresent(productName), "product name not present");
		Assert.isTrue(ValidatorUtil.isPresent(startingQty), "product qty not present");
		Assert.notNull(startingPrice, "product price not present");
		
		this.productName = productName;
		this.startingQty = startingQty;
		this.startingPrice = startingPrice;
	}
	
	@JsonPOJOBuilder(withPrefix = "")
    public static final class RegisterProductBuilder {
    }
}
