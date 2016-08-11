package com.salestock.domain.query.coupon;

import org.axonframework.common.Assert;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ap.config.persistence.nosql.mongo.AbstractDocument;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.salestock.api.dto.coupon.BaseCoupon;
import com.salestock.api.identifier.CouponId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Document
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PUBLIC)
@Data
@JsonDeserialize(builder = CouponDictionary.CouponDictionaryBuilder.class)
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class CouponDictionary extends AbstractDocument {

	private int qty;

	private BaseCoupon couponInfo;
	
	CouponDictionary(){
	}
	
	public CouponDictionary(CouponId couponId, int qty, BaseCoupon couponInfo) {
		this(couponId.getIdentifier(), qty, couponInfo, true);		
	}

	@Builder
	public CouponDictionary(String couponId, int qty, BaseCoupon couponInfo, Boolean active) {
		super(couponId, active);
		Assert.isFalse(couponInfo.isExpired(), "coupon already expired");
		this.qty = qty;
		this.couponInfo = couponInfo;
	}
	
	public void changeQty(int qty) {
		if (qty == 0) {
			this.setActive(false);
		}
		this.setQty(qty);
	}
	
	public boolean isValid() {
		return ((qty > 0) && (!couponInfo.isExpired()));
	}

	@JsonPOJOBuilder(withPrefix = "")
	public static final class CouponDictionaryBuilder {
	}
}
