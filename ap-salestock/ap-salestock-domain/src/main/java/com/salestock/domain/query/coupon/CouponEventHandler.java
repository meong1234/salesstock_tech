package com.salestock.domain.query.coupon;

import org.axonframework.common.Assert;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;

import com.ap.config.axon.util.eventcluster.SyncQueryModelNoSql;
import com.salestock.api.event.coupon.CouponDeducted;
import com.salestock.api.event.coupon.CouponRegistered;

@SyncQueryModelNoSql
public class CouponEventHandler {
	
	private CouponDictionaryMongoRepository repository;

	@Autowired
	public CouponEventHandler(CouponDictionaryMongoRepository repository) {
		Assert.notNull(repository, "repository can't null");
		this.repository = repository;
	}
	
	@EventHandler
	public void onCouponRegistered(CouponRegistered event) {
		repository
			.save(new CouponDictionary(event.getCouponId(), event.getStartingQty(), event.getInfo()));
	}
	
	@EventHandler
	public void onCouponDeducted(CouponDeducted event) {
		CouponDictionary coupon = repository.findOne(event.getCouponId().getIdentifier());
		coupon.changeQty(event.getEndQty());
		repository.save(coupon);
	}

}
