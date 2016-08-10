package com.salestock.domain.write.product;

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.common.Assert;
import org.axonframework.eventhandling.EventTemplate;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ap.config.axon.util.AbstractCommandHandler;
import com.salestock.api.command.product.DeductProduct;
import com.salestock.api.command.product.OpnameProduct;
import com.salestock.api.command.product.RegisterProduct;
import com.salestock.api.event.product.ProductError;

@Component
public class ProductCommandHandler extends AbstractCommandHandler<ProductAggregate> {

	private Repository<ProductAggregate> repository;

	private EventTemplate eventTemplate;

	public ProductCommandHandler() {
	}

	@Autowired
	public ProductCommandHandler(Repository<ProductAggregate> repository, EventTemplate eventTemplate) {

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
			throw new ProductException("product not found for id" + aggregateIdentifier.toString());
		}
	}

	@CommandHandler
	public void onRegisterProduct(RegisterProduct cmd) {
		ProductAggregate agg = new ProductAggregate(cmd);
		repository.add(agg);
	}

	@CommandHandler
	public void onOpnameProduct(OpnameProduct cmd) {
		ProductAggregate agg = this.aggLoad(cmd.getProductId());
		agg.opnameProduct(cmd.getNewQty(), cmd.getNewPrice());
	}

	@CommandHandler
	public void onDeductProduct(DeductProduct cmd) {
		try {
			ProductAggregate agg = this.aggLoad(cmd.getProductId());
			agg.deductProduct(cmd.getOrderId(), cmd.getQty());
		} catch (ProductException | IllegalArgumentException e) {
			eventTemplate.publishEvent(new ProductError(cmd.getProductId(), cmd.getOrderId(), e.getMessage()));
		}
	}

	@Override
	public void setRepository(Repository<ProductAggregate> repository) {
		this.repository = repository;
	}

	@Override
	public void setEventTemplate(EventTemplate template) {
		this.eventTemplate = template;

	}
}
