package com.ap.test;

import org.axonframework.eventhandling.EventTemplate;
import org.axonframework.eventsourcing.AggregateFactory;
import org.axonframework.eventsourcing.EventSourcedAggregateRoot;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.ap.config.axon.util.AbstractCommandHandler;

public abstract class BaseAggregateTest <T extends EventSourcedAggregateRoot>{
	
	// need to be implemented in concrete test classes
    protected abstract Class<T> getAggregateType();
    protected abstract AggregateFactory<T> getAggregateFactory();
    protected abstract AbstractCommandHandler<T> getCommandHandler();
    protected FixtureConfiguration<T> fixture; 
	protected EventTemplate eventTemplate;
    
    @Before
    public void configureTestFixture() {
        AggregateFactory<T> aggregateFactory = getAggregateFactory();
        AbstractCommandHandler<T> commandHandler = getCommandHandler();

        fixture = Fixtures.newGivenWhenThenFixture(getAggregateType());
        fixture.registerAggregateFactory(aggregateFactory);
        fixture.registerAnnotatedCommandHandler(commandHandler);
        
        eventTemplate = new EventTemplate(fixture.getEventBus());

        commandHandler.setRepository(fixture.getRepository());
        commandHandler.setEventTemplate(eventTemplate);
    }

}
