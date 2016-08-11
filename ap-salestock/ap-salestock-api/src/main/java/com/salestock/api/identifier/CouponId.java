package com.salestock.api.identifier;

import com.ap.domain.base.BaseIdentifier;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@JsonDeserialize(builder = CouponId.CouponIdBuilder.class)
@Value
public class CouponId extends BaseIdentifier{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CouponId() {
		super();
	}

	@Builder
	public CouponId(String identifier) {
		super(identifier);
	}
	
	@JsonPOJOBuilder(withPrefix = "")
    public static final class CouponIdBuilder {
    }

}
