package com.salestock.domain.query.product;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDictionaryMongoRepository extends CrudRepository<ProductDictionary, String> {

}
