package com.salestock.shared;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.axonframework.common.Assert;
import org.hibernate.validator.constraints.NotBlank;

import com.ap.misc.util.ValidatorUtil;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@EqualsAndHashCode
@Value
@ToString
@JsonDeserialize(builder = Money.MoneyBuilder.class)
public class Money {
	
	@NotBlank
	@NotNull
	private String currency;
	
	private BigDecimal value;
	
	@Builder
	public Money(String currency, BigDecimal value) {
		Assert.notEmpty(currency, "currency can't be null");
		Assert.isTrue(ValidatorUtil.isPresent(value), "value is not present");
		
		this.currency = currency;
		this.value = value;
	}
	
	public Money multiply(BigDecimal qty){
		return new Money(this.currency, this.value.multiply(qty));
	}
	
	public Money persentage(BigDecimal qty){
		return this.multiply(qty).divide(new BigDecimal(100));
	}
	
	public Money divide(BigDecimal qty){
		return new Money(this.currency, this.value.divide(qty));
	}
	
	public Money add(Money money){
		if (ValidatorUtil.isPresent(money)) {
			return new Money(this.currency, this.value.add(money.getValue()));
		} 
		return this;
	}
	
	public Money dec(Money money){
		if (ValidatorUtil.isPresent(money)) {
			return new Money(this.currency, this.value.subtract(money.getValue()));
		} 
		return this;
	}
	
	public static Money idrMoney(BigDecimal value) {
		return new Money("IDR", value);
	}

	@JsonPOJOBuilder(withPrefix = "")
    public static final class MoneyBuilder {
    }
}
