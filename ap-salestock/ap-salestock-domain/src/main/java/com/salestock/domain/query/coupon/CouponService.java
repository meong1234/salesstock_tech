package com.salestock.domain.query.coupon;

import org.axonframework.common.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import com.ap.misc.util.ValidatorUtil;

public class CouponService{

	private CouponDictionaryMongoRepository repository;

	@Autowired
	public CouponService(CouponDictionaryMongoRepository repository) {
		Assert.notNull(repository, "repository can't null");
		this.repository = repository;
	}

	public boolean isValidCoupon(String arg0) {
		CouponDictionary coupon = repository.findOne(arg0);
		if (ValidatorUtil.isPresent(coupon)) {
			return coupon.isValid();
		}
		return false;
	}
}
