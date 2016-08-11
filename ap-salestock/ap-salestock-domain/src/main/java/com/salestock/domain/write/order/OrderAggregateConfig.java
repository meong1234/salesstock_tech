package com.salestock.domain.write.order;

import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventsourcing.AggregateFactory;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.GenericAggregateFactory;
import org.axonframework.eventsourcing.SnapshotterTrigger;
import org.axonframework.eventstore.EventStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderAggregateConfig {
	
	@Bean
    AggregateFactory<OrderAggregate> orderAggregateFactory() {
        return new GenericAggregateFactory<>(OrderAggregate.class);
    }
	
	@Bean(name = "orderAggregateRepository")
	public EventSourcingRepository<OrderAggregate> orderAggregateRepository(EventBus eventBus, EventStore eventStore, SnapshotterTrigger snapshotterTrigger) {
		EventSourcingRepository<OrderAggregate> repository = new EventSourcingRepository<>(OrderAggregate.class, eventStore);
		repository.setEventBus(eventBus);
		repository.setSnapshotterTrigger(snapshotterTrigger);
		return repository;
	}

}
