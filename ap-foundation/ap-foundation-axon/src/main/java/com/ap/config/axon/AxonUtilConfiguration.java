package com.ap.config.axon;

import org.axonframework.auditing.AuditDataProvider;
import org.axonframework.auditing.AuditingInterceptor;
import org.axonframework.cache.Cache;
import org.axonframework.cache.WeakReferenceCache;
import org.axonframework.commandhandling.interceptors.BeanValidationInterceptor;
import org.axonframework.commandhandling.interceptors.LoggingInterceptor;
import org.axonframework.common.annotation.DefaultParameterResolverFactory;
import org.axonframework.common.annotation.MultiParameterResolverFactory;
import org.axonframework.common.annotation.ParameterResolverFactory;
import org.axonframework.common.annotation.SpringBeanParameterResolverFactory;
import org.axonframework.domain.IdentifierFactory;
import org.axonframework.eventhandling.OrderResolver;
import org.axonframework.eventhandling.SpringAnnotationOrderResolver;
import org.axonframework.eventhandling.replay.BackloggingIncomingMessageHandler;
import org.axonframework.serializer.Serializer;
import org.axonframework.serializer.xml.XStreamSerializer;
import org.axonframework.unitofwork.NoTransactionManager;
import org.axonframework.unitofwork.SpringTransactionManager;
import org.axonframework.unitofwork.TransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.ap.config.axon.util.CommandMetadataAuditDataProvider;
import com.ap.config.axon.util.correlationevent.CorrelationCommandHandlerInterceptor;
import com.ap.config.xtream.XtreamLocalDateConverter;
import com.ap.config.xtream.XtreamLocalDateTimeConverter;
import com.thoughtworks.xstream.XStream;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class AxonUtilConfiguration {

//	 @Bean
//	 Serializer axonJsonSerializer() {
//	 return new JacksonSerializer();
//	 }
	
	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@Bean(name="withTransaction")
	TransactionManager sqlTransaction(){
		TransactionManager txMgr = new SpringTransactionManager(transactionManager);
        return txMgr;
    }
	
	@Bean(name="noTransaction")
	TransactionManager noSqlTransaction(){
		TransactionManager txMgr = new NoTransactionManager();
        return txMgr;
    }

	@Bean
	@ConditionalOnMissingBean
	public Serializer serializer() {
		XStream xstream = new XStream(); 
		xstream.registerConverter( new XtreamLocalDateConverter());
		xstream.registerConverter(new XtreamLocalDateTimeConverter());
		//new XStreamSerializer (xstream); 
		return new XStreamSerializer (xstream);
	}

	@Bean
	@ConditionalOnMissingBean
	public Cache cache() {
		return new WeakReferenceCache();
	}

	@Bean
	public IdentifierFactory identifierFactory() {
		return IdentifierFactory.getInstance();
	}

	@Bean
	LocalValidatorFactoryBean validator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		// localValidatorFactoryBean.setMappingLocations(new
		// ClassPathResource("/validation/custom-constraints.xml"));
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Bean
	BeanValidationInterceptor beanValidationInterceptor() {
		return new BeanValidationInterceptor(validator());
	}

	@Bean
	public AuditingInterceptor auditingInterceptor() {
		final AuditingInterceptor auditingInterceptor = new AuditingInterceptor();

		// Attach user info to all events:
		auditingInterceptor.setAuditDataProvider(auditDataProvider());

		return auditingInterceptor;
	}

	@Bean
	public AuditDataProvider auditDataProvider() {
		return new CommandMetadataAuditDataProvider();
	}

	@Bean
	LoggingInterceptor loggingInterceptor() {
		return new LoggingInterceptor();
	}
	
	@Bean
	CorrelationCommandHandlerInterceptor correlationCommandHandlerInterceptor() {
		return new CorrelationCommandHandlerInterceptor();
	}
	
	
	//used on eventbuss
	@Bean(name="SpringAnnotationOrderResolver")
	public OrderResolver orderResolver() {
		return new SpringAnnotationOrderResolver();
	}
	
	@Bean
    public ParameterResolverFactory parameterResolverFactory() {
        return new MultiParameterResolverFactory(new SpringBeanParameterResolverFactory(), new DefaultParameterResolverFactory());
    }
	
	@Bean
    public BackloggingIncomingMessageHandler backloggingIncomingMessageHandler() {
        return new BackloggingIncomingMessageHandler();
    }	
}
