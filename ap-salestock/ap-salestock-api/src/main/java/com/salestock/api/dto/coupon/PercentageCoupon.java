package com.salestock.api.dto.coupon;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import org.axonframework.common.Assert;

import com.ap.misc.util.ValidatorUtil;
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
@JsonDeserialize(builder = PercentageCoupon.PercentageCouponBuilder.class)
@Data
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class PercentageCoupon extends BaseCoupon {
	
	private BigDecimal persentege;
	
	@Builder
	public PercentageCoupon(OffsetDateTime issuedDate, OffsetDateTime expiredDate, BigDecimal persentege) {
		super(issuedDate, expiredDate);
		
		Assert.isTrue(ValidatorUtil.isPresent(persentege), "persentege can't be null");
		this.persentege = persentege;
	}

	@Override
	public Money calculateDiscount(Money price) {
		Assert.notNull(price, "price can't be null");
		return price.persentage(this.persentege);
	}
	
	@JsonPOJOBuilder(withPrefix = "")
    public static final class PercentageCouponBuilder {
    }

}
