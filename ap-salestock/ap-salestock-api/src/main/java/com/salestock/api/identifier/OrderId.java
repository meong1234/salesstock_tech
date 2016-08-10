package com.salestock.api.identifier;

import com.ap.domain.base.BaseIdentifier;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonDeserialize(builder = OrderId.OrderIdBuilder.class)
@Value
public class OrderId extends BaseIdentifier{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public OrderId() {
		super();
	}

	@Builder
	public OrderId(String identifier) {
		super(identifier);
	}
	
	@JsonPOJOBuilder(withPrefix = "")
    public static final class OrderIdBuilder {
    }


}
