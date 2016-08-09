package com.salestock.shared;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.ap.misc.util.ValidatorUtil;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

@Value
@AllArgsConstructor
@ToString
@Builder
@JsonDeserialize(builder = Money.MoneyBuilder.class)
public class Money {
	
	@NotBlank
	@NotNull
	private String currency;
	
	private BigDecimal value;
	
	
	
	public Money multiply(int qty){
		return new Money(this.currency, this.value.multiply(new BigDecimal(qty)));
	}
	
	public Money add(Money money){
		if (ValidatorUtil.isPresent(money)) {
			return new Money(this.currency, this.value.add(money.getValue()));
		} 
		return this;
	}
	
	@JsonPOJOBuilder(withPrefix = "")
    public static final class MoneyBuilder {
    }
}
