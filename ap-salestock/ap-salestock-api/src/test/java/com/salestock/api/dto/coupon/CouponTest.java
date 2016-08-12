package com.salestock.api.dto.coupon;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import org.junit.Test;

import com.salestock.shared.Money;

public class CouponTest {

	@Test
	public void testCalculateDiscountOnNominal() {
		NominalCoupon coupon = new NominalCoupon(OffsetDateTime.now(), OffsetDateTime.now().plusDays(1), Money.idrMoney(new BigDecimal(1000)));
		Money discount = coupon.calculateDiscount(Money.idrMoney(new BigDecimal(10000)));
		assertEquals(Money.idrMoney(new BigDecimal(1000)), discount);
	}
	
	@Test
	public void testCalculateDiscountOnNominalHigherThanBasePrice() {
		NominalCoupon coupon = new NominalCoupon(OffsetDateTime.now(), OffsetDateTime.now().plusDays(1), Money.idrMoney(new BigDecimal(1000)));
		Money discount = coupon.calculateDiscount(Money.idrMoney(new BigDecimal(100)));
		assertEquals(Money.idrMoney(new BigDecimal(100)), discount);
	}
	
	@Test
	public void testCalculateDiscountOnPercentage() {
		PercentageCoupon coupon = new PercentageCoupon(OffsetDateTime.now(), OffsetDateTime.now().plusDays(1), new BigDecimal(20));
		Money discount = coupon.calculateDiscount(Money.idrMoney(new BigDecimal(10000)));
		assertEquals(Money.idrMoney(new BigDecimal(2000)), discount);
	}
	
	@Test
	public void testExpiredCoupon() {
		PercentageCoupon coupon = new PercentageCoupon(OffsetDateTime.now().minusDays(2), OffsetDateTime.now().minusDays(1), new BigDecimal(20));
		assertEquals(true, coupon.isExpired());
	}

}
