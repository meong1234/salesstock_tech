package com.salestock.domain.write.product;

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.common.Assert;
import org.axonframework.eventhandling.EventTemplate;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salestock.api.command.product.OpnameProduct;
import com.salestock.api.command.product.RegisterProduct;
import com.salestock.api.command.product.SellProduct;
import com.salestock.api.event.product.SellProductFailed;

@Component
public class ProductCommandHandler {
	
	private Repository<ProductAggregate> repository;
	
	private EventTemplate eventTemplate;

	@Autowired
	public ProductCommandHandler(Repository<ProductAggregate> repository,
			EventTemplate eventTemplate) {
		
		Assert.notNull(repository, "product repository is null");
		Assert.notNull(eventTemplate, "eventTemplate is null");
		
		this.repository = repository;
		this.eventTemplate = eventTemplate;
	}
	
	private ProductAggregate aggLoad(Object aggregateIdentifier) {
		try {
			ProductAggregate agg = repository.load(aggregateIdentifier);
			return agg;
		} catch (AggregateNotFoundException e) {
			throw new ProductException("product not found for id"+aggregateIdentifier.toString());
		}
	}
	
	@CommandHandler
	public void onRegisterProduct(RegisterProduct cmd) {
		ProductAggregate agg = new ProductAggregate(cmd.getProductName(), cmd.getStartingQty(), cmd.getStartingPrice());
		repository.add(agg);
	}
	
	@CommandHandler
	public void onOpnameProduct(OpnameProduct cmd) {
		ProductAggregate agg = this.aggLoad(cmd.getProductId());
		agg.opnameProduct(cmd.getNewQty(), cmd.getNewPrice());
	}
	
	@CommandHandler
	public void onSellProduct(SellProduct cmd) {
		try {
			ProductAggregate agg = this.aggLoad(cmd.getProductId());
			agg.sellProduct(cmd.getQty());
		} catch (IllegalArgumentException e) {
			eventTemplate.publishEvent(new SellProductFailed(cmd.getProductId(), e.getMessage()));
		}
	}

}
