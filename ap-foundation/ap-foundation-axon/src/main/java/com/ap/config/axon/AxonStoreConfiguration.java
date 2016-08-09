package com.ap.config.axon;

import java.sql.SQLException;
import java.util.concurrent.Executor;

import javax.sql.DataSource;

import org.axonframework.cache.Cache;
import org.axonframework.common.jdbc.DataSourceConnectionProvider;
import org.axonframework.common.jdbc.UnitOfWorkAwareConnectionProviderWrapper;
import org.axonframework.eventsourcing.EventCountSnapshotterTrigger;
import org.axonframework.eventsourcing.Snapshotter;
import org.axonframework.eventsourcing.SnapshotterTrigger;
import org.axonframework.eventsourcing.SpringAggregateSnapshotter;
import org.axonframework.eventstore.EventStore;
import org.axonframework.eventstore.SnapshotEventStore;
import org.axonframework.eventstore.jdbc.DefaultEventEntryStore;
import org.axonframework.eventstore.jdbc.EventEntryStore;
import org.axonframework.eventstore.jdbc.JdbcEventStore;
import org.axonframework.eventstore.jdbc.PostgresEventSqlSchema;
import org.axonframework.eventstore.jdbc.SchemaConfiguration;
import org.axonframework.eventstore.jpa.SQLStateResolver;
import org.axonframework.serializer.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.extern.slf4j.Slf4j;

@ConditionalOnClass(EventStore.class)
@Configuration
@Slf4j
public class AxonStoreConfiguration {
	
	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@Autowired
	Serializer axonSerializer;
	
	@Autowired
	Cache cache;
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	@Qualifier("taskExecutor")
	Executor getAsyncExecutor;
	
	
	@Bean
	 @ConditionalOnMissingBean
    public JdbcEventStore jdbcEventStore() throws SQLException {
		log.info("setup eventStore: "+dataSource.toString());
		
        UnitOfWorkAwareConnectionProviderWrapper uowAwareConnectionProvider =
                new UnitOfWorkAwareConnectionProviderWrapper(new DataSourceConnectionProvider(dataSource));

        EventEntryStore<?> eventEntryStore =
                new DefaultEventEntryStore<>(uowAwareConnectionProvider,
                        new PostgresEventSqlSchema<>(byte[].class,
                                new SchemaConfiguration("domainevententry", "snapshotevententry")));

        JdbcEventStore jdbcEventStore = new JdbcEventStore(eventEntryStore, axonSerializer);
        jdbcEventStore.setPersistenceExceptionResolver(new SQLStateResolver());
        jdbcEventStore.setBatchSize(100);
        jdbcEventStore.setMaxSnapshotsArchived(1);
        return jdbcEventStore;
    }
	
	@Bean
	public EventStore eventStore() throws SQLException {
		return jdbcEventStore();
	}
	
	@Bean
    public Snapshotter snapshotter(EventStore eventStore) {
		log.debug("setup snapshotter");
		SpringAggregateSnapshotter snapshotter = new SpringAggregateSnapshotter();
        snapshotter.setEventStore((SnapshotEventStore) eventStore);
        snapshotter.setExecutor(getAsyncExecutor);
        return snapshotter;
    }
	
	
	@Bean
    public SnapshotterTrigger snapshottrigger(Snapshotter snapshotter) {
		log.debug("setup snapshottrigger");
        EventCountSnapshotterTrigger snapshotterTrigger = new EventCountSnapshotterTrigger();
        snapshotterTrigger.setTrigger(2);
        snapshotterTrigger.setSnapshotter(snapshotter);
        snapshotterTrigger.setAggregateCache(cache);
        return snapshotterTrigger;
    }
}
