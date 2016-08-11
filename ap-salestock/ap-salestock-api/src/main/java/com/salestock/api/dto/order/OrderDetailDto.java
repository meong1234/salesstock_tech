package com.salestock.api.dto.order;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.salestock.api.BaseProductObject;
import com.salestock.api.identifier.ProductId;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonDeserialize(builder = OrderDetailDto.OrderDetailDtoBuilder.class)
@Value
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class OrderDetailDto extends BaseProductObject {

	private int qty;
	
	@Builder
	public OrderDetailDto(ProductId productId, int qty) {
		super(productId);
		this.qty = qty;
	}

	@JsonPOJOBuilder(withPrefix = "")
    public static final class OrderDetailDtoBuilder {
    }

}
