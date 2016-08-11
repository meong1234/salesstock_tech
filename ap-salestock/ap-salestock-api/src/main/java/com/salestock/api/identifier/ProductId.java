package com.salestock.api.identifier;

import com.ap.domain.base.BaseIdentifier;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@JsonDeserialize(builder = ProductId.ProductIdBuilder.class)
@Value
public class ProductId extends BaseIdentifier{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ProductId() {
		super();
	}

	@Builder
	public ProductId(String identifier) {
		super(identifier);
	}
	
	@JsonPOJOBuilder(withPrefix = "")
    public static final class ProductIdBuilder {
    }

}
