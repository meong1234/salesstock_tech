package com.ap.config.axon;

import java.util.concurrent.Executor;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.axonframework.eventhandling.AnnotationClusterSelector;
import org.axonframework.eventhandling.AutowiringClusterSelector;
import org.axonframework.eventhandling.ClassNamePatternClusterSelector;
import org.axonframework.eventhandling.Cluster;
import org.axonframework.eventhandling.ClusterSelector;
import org.axonframework.eventhandling.ClusteringEventBus;
import org.axonframework.eventhandling.CompositeClusterSelector;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.EventTemplate;
import org.axonframework.eventhandling.OrderResolver;
import org.axonframework.eventhandling.SimpleCluster;
import org.axonframework.eventhandling.annotation.AnnotationEventListenerBeanPostProcessor;
import org.axonframework.eventhandling.async.AsynchronousCluster;
import org.axonframework.eventhandling.async.SequentialPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ap.config.axon.util.eventcluster.AsyncQueryModelNoSql;
import com.ap.config.axon.util.eventcluster.AsyncQueryModelSql;
import com.ap.config.axon.util.eventcluster.RetryableClusterDecorator;
import com.ap.config.axon.util.eventcluster.SyncQueryModelNoSql;
import com.ap.config.axon.util.eventcluster.SyncQueryModelSql;
import com.ap.config.axon.util.eventcluster.TransactionalClusterDecorator;
import com.ap.config.base.FoundationProperties;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;


@ConditionalOnClass(EventBus.class)
@Configuration
@Slf4j
public class AxonSimpleEventConfiguration {
	
	@Autowired
	@Qualifier("SpringAnnotationOrderResolver")
	OrderResolver orderResolver;
	
	@Autowired
	@Qualifier("taskExecutor")
	Executor getAsyncExecutor;
	
	private static final Pattern SAGA_PATTERN = Pattern.compile(".*Saga");
	public static final String SAGA_QUEUE = "local.saga";	
	public static final String SYNC_QUERY_MODEL_SQL = "local.SYNC_QUERY_MODEL_SQL";
	public static final String ASYNC_QUERY_MODEL_SQL = "local.ASYNC_QUERY_MODEL_SQL";
	public static final String SYNC_QUERY_MODEL_NOSQL = "local.SYNC_QUERY_MODEL_NOSQL";
	public static final String ASYNC_QUERY_MODEL_NOSQL = "local.SYNC_QUERY_MODEL_NOSQL";
	
	public Cluster createCluster(String queueName) {
		log.info("createCluster : "+queueName);
		Cluster cluster = new SimpleCluster(queueName, orderResolver);
		return cluster;
	}
	
	public Cluster createAyncCluster(String queueName) {
		log.info("create Async Cluster : "+queueName);
		Cluster cluster = new AsynchronousCluster(queueName, getAsyncExecutor, new SequentialPolicy());		
		return cluster;
	}
	
	@Bean
	public Cluster sagaCluster() {
		return new TransactionalClusterDecorator(createCluster(SAGA_QUEUE));
	}
	
	@Bean
	public Cluster syncSqlModel() {
		return new RetryableClusterDecorator(new TransactionalClusterDecorator(createCluster(SYNC_QUERY_MODEL_SQL)));
	}
	
	@Bean
	public Cluster asyncSqlModel() {
		return new RetryableClusterDecorator(new TransactionalClusterDecorator(createAyncCluster(ASYNC_QUERY_MODEL_SQL)));
	}
	
	@Bean
	public Cluster syncNoSqlModel() {
		return new RetryableClusterDecorator(createCluster(SYNC_QUERY_MODEL_NOSQL));
	}
	
	@Bean
	public Cluster asyncNoSqlModel() {
		return new RetryableClusterDecorator(createAyncCluster(ASYNC_QUERY_MODEL_NOSQL));
	}
	
	@Bean
    public ClusterSelector sagaClusterSelector() {
        return new ClassNamePatternClusterSelector(SAGA_PATTERN, sagaCluster());
    }
	
	@Bean
    public ClusterSelector syncSqlCluster() {
        Cluster queryModelCluster = syncSqlModel();
        return new CompositeClusterSelector(Lists.<ClusterSelector>newArrayList(
                new AnnotationClusterSelector(SyncQueryModelSql.class, queryModelCluster, true)));
    }
	
	@Bean
    public ClusterSelector asyncSqlCluster() {
        Cluster queryModelCluster = asyncSqlModel();
        return new CompositeClusterSelector(Lists.<ClusterSelector>newArrayList(
                new AnnotationClusterSelector(AsyncQueryModelSql.class, queryModelCluster, true)));
    }
	
	@Bean
    public ClusterSelector syncNoSqlCluster() {
        Cluster queryModelCluster = syncNoSqlModel();
        return new CompositeClusterSelector(Lists.<ClusterSelector>newArrayList(
                new AnnotationClusterSelector(SyncQueryModelNoSql.class, queryModelCluster, true)));
    }
	
	@Bean
    public ClusterSelector asyncNoSqlCluster() {
        Cluster queryModelCluster = asyncNoSqlModel();
        return new CompositeClusterSelector(Lists.<ClusterSelector>newArrayList(
                new AnnotationClusterSelector(AsyncQueryModelNoSql.class, queryModelCluster, true)));
    }
	
	@Bean
    public EventBus eventBus() {
		log.info("setup ClusteringEventBus : simple terminal");
        return new ClusteringEventBus(clusterSelector());
    }

    @Bean
    public ClusterSelector clusterSelector() {
        return new AutowiringClusterSelector();
    }

    @Bean
    public AnnotationEventListenerBeanPostProcessor
    annotationEventListenerBeanPostProcessor() {
        return new AnnotationEventListenerBeanPostProcessor();
    }
    
    @Bean
	public EventTemplate eventTemplate(EventBus eventBus) {
		return new EventTemplate(eventBus);
	}

}
