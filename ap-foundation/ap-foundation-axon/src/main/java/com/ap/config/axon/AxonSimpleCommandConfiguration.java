package com.ap.config.axon;

import java.util.concurrent.Executor;

import org.axonframework.auditing.AuditingInterceptor;
import org.axonframework.commandhandling.AsynchronousCommandBus;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.annotation.AnnotationCommandHandlerBeanPostProcessor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.CommandGatewayFactoryBean;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.commandhandling.interceptors.BeanValidationInterceptor;
import org.axonframework.commandhandling.interceptors.LoggingInterceptor;
import org.axonframework.unitofwork.TransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ap.config.axon.util.MyCommandGateway;
import com.ap.config.axon.util.correlationevent.CorrelationCommandHandlerInterceptor;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

@ConditionalOnClass(CommandBus.class)
@Configuration
@Slf4j
public class AxonSimpleCommandConfiguration {

	@Autowired
	@Qualifier("withTransaction")
	private TransactionManager sqlTransaction;
	
	@Autowired
	LoggingInterceptor LoggingInterceptor;
	
	@Autowired
	BeanValidationInterceptor beanValidationInterceptor;
	
	@Autowired
	CorrelationCommandHandlerInterceptor correlationCommandHandlerInterceptor;
	
	@Autowired
	AuditingInterceptor auditingInterceptor;
	
	@Autowired
	LoggingInterceptor loggingInterceptor;

	@Autowired
	@Qualifier("taskExecutor")
	Executor getAsyncExecutor;
	

	@Bean
	@ConditionalOnMissingBean
	CommandBus commandBus() {
		log.info("commandBus : AsynchronousCommandBus");
		AsynchronousCommandBus commandBus = new AsynchronousCommandBus(getAsyncExecutor);
		commandBus.setDispatchInterceptors(Lists.newArrayList(beanValidationInterceptor));
		commandBus.setHandlerInterceptors(Lists.newArrayList(auditingInterceptor, correlationCommandHandlerInterceptor, loggingInterceptor));
		commandBus.setTransactionManager(sqlTransaction);
		return commandBus;
	}

	@Bean
	@ConditionalOnMissingBean
	AnnotationCommandHandlerBeanPostProcessor commandHandlerBeanPostProcessor() {
		AnnotationCommandHandlerBeanPostProcessor proc = new AnnotationCommandHandlerBeanPostProcessor();
		proc.setCommandBus(commandBus());
		return proc;
	}

	@Bean
	@ConditionalOnMissingBean
	public CommandGateway commandGateway() {
		return new DefaultCommandGateway(commandBus());
	}
	
	@Bean(name="ctmCommandGateway")
	public CommandGatewayFactoryBean<MyCommandGateway> ctmCommandGateway() {
		CommandGatewayFactoryBean<MyCommandGateway> factory = new CommandGatewayFactoryBean<MyCommandGateway>();
		factory.setGatewayInterface(MyCommandGateway.class);
		factory.setCommandBus(commandBus());
		return factory;
	}	

	/*
	 * @Bean public CommandGatewayFactoryBean<CommandGateway>
	 * commandGatewayFactoryBean(CommandBus commandBus) {
	 * CommandGatewayFactoryBean<CommandGateway> factory = new
	 * CommandGatewayFactoryBean<CommandGateway>();
	 * factory.setCommandBus(commandBus); return factory; }
	 */

}
