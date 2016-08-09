package com.salestock.domain.write.product;

import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventsourcing.AggregateFactory;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.GenericAggregateFactory;
import org.axonframework.eventsourcing.SnapshotterTrigger;
import org.axonframework.eventstore.EventStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductAggregateConfig {
	
	@Bean
    AggregateFactory<ProductAggregate> productAggregateFactory() {
        return new GenericAggregateFactory<>(ProductAggregate.class);
    }
	
	@Bean(name = "productAggregateRepository")
	public EventSourcingRepository<ProductAggregate> productAggregateRepository(EventBus eventBus, EventStore eventStore, SnapshotterTrigger snapshotterTrigger) {
		EventSourcingRepository<ProductAggregate> repository = new EventSourcingRepository<>(ProductAggregate.class, eventStore);
		repository.setEventBus(eventBus);
		repository.setSnapshotterTrigger(snapshotterTrigger);
		return repository;
	}

}
