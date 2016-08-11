package com.salestock.domain.query.coupon;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponDictionaryMongoRepository extends CrudRepository<CouponDictionary, String> {
}
