package com.salestock.domain.query.coupon;

import java.math.BigDecimal;
import java.util.Optional;

import org.axonframework.common.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ap.misc.util.ValidatorUtil;
import com.salestock.domain.query.product.ProductDictionary;
import com.salestock.shared.Money;

@Service
public class CouponService{

	private CouponDictionaryMongoRepository repository;

	@Autowired
	public CouponService(CouponDictionaryMongoRepository repository) {
		Assert.notNull(repository, "repository can't null");
		this.repository = repository;
	}
	
	public Optional<CouponDictionary> find(String arg0) {
		return Optional.of(repository.findOne(arg0));
	}

	public boolean isValidCoupon(String arg0) {
		CouponDictionary coupon = repository.findOne(arg0);
		if (ValidatorUtil.isPresent(coupon)) {
			return coupon.isValid();
		}
		return false;
	}
	
	public Money calculateDiscount(String arg0, Money price) {
		CouponDictionary coupon = repository.findOne(arg0);
		if (ValidatorUtil.isPresent(coupon)) {
			return coupon.getCouponInfo().calculateDiscount(price);
		}
		return Money.idrMoney(BigDecimal.ZERO);
	}
}
