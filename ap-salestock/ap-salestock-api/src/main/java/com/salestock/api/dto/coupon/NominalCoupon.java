package com.salestock.api.dto.coupon;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.salestock.shared.Money;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonDeserialize(builder = NominalCoupon.NominalCouponBuilder.class)
@Data
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class NominalCoupon extends BaseCoupon {
	
	private Money nominalDiscount;
	
	@Builder
	public NominalCoupon(OffsetDateTime issuedDate, OffsetDateTime expiredDate, Money nominalDiscount) {
		super(issuedDate, expiredDate);
		this.nominalDiscount = nominalDiscount;
	}

	@Override
	public Money calculateDiscount(Money price) {
		return this.nominalDiscount;
	}
	
	@JsonPOJOBuilder(withPrefix = "")
    public static final class NominalCouponBuilder {
    }

}
