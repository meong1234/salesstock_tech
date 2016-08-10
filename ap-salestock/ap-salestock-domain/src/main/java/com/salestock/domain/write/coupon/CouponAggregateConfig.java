package com.salestock.domain.write.coupon;

import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventsourcing.AggregateFactory;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.GenericAggregateFactory;
import org.axonframework.eventsourcing.SnapshotterTrigger;
import org.axonframework.eventstore.EventStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CouponAggregateConfig {

	@Bean
    AggregateFactory<CouponAggregate> couponAggregateFactory() {
        return new GenericAggregateFactory<>(CouponAggregate.class);
    }
	
	@Bean(name = "couponAggregateRepository")
	public EventSourcingRepository<CouponAggregate> couponAggregateRepository(EventBus eventBus, EventStore eventStore, SnapshotterTrigger snapshotterTrigger) {
		EventSourcingRepository<CouponAggregate> repository = new EventSourcingRepository<>(CouponAggregate.class, eventStore);
		repository.setEventBus(eventBus);
		repository.setSnapshotterTrigger(snapshotterTrigger);
		return repository;
	}
}
