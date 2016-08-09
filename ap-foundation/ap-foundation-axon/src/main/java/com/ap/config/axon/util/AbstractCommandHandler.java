package com.ap.config.axon.util;

import org.axonframework.eventsourcing.EventSourcedAggregateRoot;
import org.axonframework.repository.Repository;

public abstract class AbstractCommandHandler<T extends EventSourcedAggregateRoot> {
	
	public abstract void setRepository(Repository<T> repository);
	public abstract void setService(Object service);

}
