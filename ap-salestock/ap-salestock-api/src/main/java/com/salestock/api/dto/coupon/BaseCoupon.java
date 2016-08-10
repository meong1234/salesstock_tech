package com.salestock.api.dto.coupon;

import java.time.OffsetDateTime;

import org.axonframework.common.Assert;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.salestock.shared.Money;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@JsonTypeInfo(  
	    use = JsonTypeInfo.Id.NAME,  
	    include = JsonTypeInfo.As.PROPERTY,  
	    property = "couponType",
	    visible = true)  
	@JsonSubTypes({  
	    @Type(value = NominalCoupon.class, name = "nominal"),
	    @Type(value = PercentageCoupon.class, name = "percentage")}) 

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public abstract class BaseCoupon {
	
	protected OffsetDateTime issuedDate;
	
	protected OffsetDateTime expiredDate;
	
	public BaseCoupon(OffsetDateTime issuedDate, OffsetDateTime expiredDate) {
		Assert.notNull(issuedDate, "issuedDate can't null");
		Assert.notNull(expiredDate, "expiredDate can't null");
		Assert.isTrue(expiredDate.isAfter(issuedDate), "expiredDate must after issuedDate");
		
		this.issuedDate = issuedDate;
		this.expiredDate = expiredDate;
	}
	
	public BaseCoupon(OffsetDateTime expiredDate) {
		this(OffsetDateTime.now(), expiredDate);
	}

	public boolean isExpired() {
		return OffsetDateTime.now().isAfter(this.expiredDate);
	}

	public abstract Money calculateDiscount(Money price);

}
