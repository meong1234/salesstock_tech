package com.ap.config.axon;

import javax.sql.DataSource;

import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.scheduling.EventScheduler;
import org.axonframework.eventhandling.scheduling.quartz.QuartzEventScheduler;
import org.axonframework.saga.GenericSagaFactory;
import org.axonframework.saga.ResourceInjector;
import org.axonframework.saga.SagaFactory;
import org.axonframework.saga.SagaRepository;
import org.axonframework.saga.repository.jdbc.JdbcSagaRepository;
import org.axonframework.saga.repository.jdbc.PostgresSagaSqlSchema;
import org.axonframework.saga.spring.SpringResourceInjector;
import org.axonframework.serializer.Serializer;
import org.axonframework.unitofwork.SpringTransactionManager;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@ConditionalOnClass(SagaRepository.class)
@Configuration
@Slf4j
public class AxonSagaConfiguration {
	
	@Autowired
	Serializer axonSerializer;
	
	@Bean
    public ResourceInjector resourceInjector() {
        return new SpringResourceInjector();
    }
	
	@Bean
	@ConditionalOnMissingBean
    public SagaRepository sagaRepository(DataSource dataSource) {
		log.info("setup sagaRepository: "+dataSource.toString());
        JdbcSagaRepository sagaRepository = new JdbcSagaRepository(dataSource, new PostgresSagaSqlSchema());
        sagaRepository.setResourceInjector(resourceInjector());
        sagaRepository.setSerializer(axonSerializer);
        return sagaRepository;
    }
	
	@Bean
    public SagaFactory sagaFactory() {
		log.info("setup sagaFactory: ");
        GenericSagaFactory sagaFactory = new GenericSagaFactory();
        sagaFactory.setResourceInjector(resourceInjector());
        return sagaFactory;
    }
	
	@Bean
	public EventScheduler eventScheduler(SpringTransactionManager springTransactionManager, EventBus eventBus,
			Scheduler scheduler) throws SchedulerException {
		log.info("setup eventScheduler: ");
		QuartzEventScheduler eventScheduler = new QuartzEventScheduler();
		eventScheduler.setEventBus(eventBus);
		eventScheduler.setTransactionManager(springTransactionManager);
		eventScheduler.setScheduler(scheduler);
		eventScheduler.initialize();
		return eventScheduler;
	}

}
