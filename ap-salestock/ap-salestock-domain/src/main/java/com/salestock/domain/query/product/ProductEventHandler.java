package com.salestock.domain.query.product;

import org.axonframework.common.Assert;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;

import com.ap.config.axon.util.eventcluster.SyncQueryModelNoSql;
import com.ap.misc.util.ValidatorUtil;
import com.salestock.api.event.product.ProductDeducted;
import com.salestock.api.event.product.ProductOpnamed;
import com.salestock.api.event.product.ProductRegistered;

@SyncQueryModelNoSql
public class ProductEventHandler {

	private ProductDictionaryMongoRepository repository;

	@Autowired
	public ProductEventHandler(ProductDictionaryMongoRepository repository) {
		Assert.notNull(repository, "repository can't null");
		this.repository = repository;
	}
	
	@EventHandler
	public void onProductRegistered(ProductRegistered event) {
		repository
			.save(new ProductDictionary(event.getProductId(), event.getProductName(), event.getStartingQty(), event.getStartingPrice()));
	}
	
	@EventHandler
	public void onProductOpnamed(ProductOpnamed event) {
		ProductDictionary product = repository.findOne(event.getProductId().getIdentifier());
		if (ValidatorUtil.isPresent(product)) {
			product.opName(event.getNewQty(), event.getNewPrice());
		}
	}
	
	
	@EventHandler
	public void onProductDeducted(ProductDeducted event) {
		ProductDictionary product = repository.findOne(event.getProductId().getIdentifier());
		if (ValidatorUtil.isPresent(product)) {
			product.setQty(event.getEndQty());
		}
	}
	
}
