package com.salestock.domain.write;

import org.axonframework.eventhandling.EventBus;
import org.axonframework.saga.SagaFactory;
import org.axonframework.saga.SagaManager;
import org.axonframework.saga.SagaRepository;
import org.axonframework.saga.annotation.AnnotatedSagaManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.salestock.domain.write.order.OrderSubmittedSaga;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class DomainSagaManager {

	@Bean
    public SagaManager sagaManager(EventBus eventBus, SagaFactory sagaFactory, SagaRepository sagaRepository)  {
		log.info("setup sagaManager: ");
        AnnotatedSagaManager sagaManager = new AnnotatedSagaManager(sagaRepository, sagaFactory, eventBus, OrderSubmittedSaga.class);
        eventBus.subscribe(sagaManager);
        return sagaManager;
    }
}
