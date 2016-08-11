package com.salestock.domain.query.coupon;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ap.config.base.FoundationProperties;
import com.ap.config.persistence.nosql.mongo.MongoConfiguration;
import com.salestock.api.event.coupon.CouponDeducted;
import com.salestock.api.event.coupon.CouponRegistered;
import com.salestock.domain.write.coupon.CouponCommandHandlerTest;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(initializers=ConfigFileApplicationContextInitializer.class,
classes = { FoundationProperties.class, MongoConfiguration.class, CouponDictionaryMongoRepository.class})
@ActiveProfiles("dev")
public class CouponEventHandlerTest{
	
	@Autowired
	private CouponDictionaryMongoRepository repository;
	
	private CouponEventHandler couponEventHandler;
	
	private CouponService couponService;
	
	@Before
	public void setUp(){
		this.couponEventHandler = new CouponEventHandler(repository);
		this.couponService = new CouponService(repository);
	}

	@Test
	public void testonCouponRegistered() {		
		CouponRegistered event = CouponCommandHandlerTest
			.createCouponRegistered(CouponCommandHandlerTest.createRegisterCoupon(10));
		
		couponEventHandler.onCouponRegistered(event);
		assertEquals(true, couponService.isValidCoupon(event.getCouponId().getIdentifier()));
	}
	
	@Test
	public void testonCouponDeducted() {	
		CouponDictionary before = repository.findOne("couponId");
		CouponDeducted event = CouponCommandHandlerTest.createCouponDeducted();
		couponEventHandler.onCouponDeducted(event);
		CouponDictionary after = repository.findOne("couponId");
		assertEquals(before.getQty()-1, after.getQty());
	}

}
