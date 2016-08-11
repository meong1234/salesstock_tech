package com.ap.config.persistence.nosql.mongo;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Component;

import com.ap.config.persistence.nosql.mongo.jsr310.OffsetDateTimeToStringConverter;
import com.ap.config.persistence.nosql.mongo.jsr310.StringToOffsetDateTimeConverter;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;

import lombok.extern.slf4j.Slf4j;

@EnableMongoRepositories(value = {
		"com" }, includeFilters = @ComponentScan.Filter(pattern = "..*MongoRepository", type = FilterType.REGEX))
@Component
@Slf4j
public class MongoConfiguration  {

	@Inject
	private Environment environment;

	@Bean
	//@Override
	public Mongo mongo() throws Exception {
		return mongoClient();
	}

	@Bean
	public MongoClient mongoClient() throws UnknownHostException {
		log.debug("setup mongoClient");
		
		// location of db
		ServerAddress sa = new ServerAddress(environment.getProperty("mongodb.host"),
				environment.getProperty("mongodb.port", Integer.class));

		// set optional default parameters here
		MongoClientOptions.Builder builder = MongoClientOptions.builder();

		// none yet

		MongoClientOptions options = builder.build();

		return new MongoClient(sa, options);
	}

	@Bean
	//@Override
	public MongoDbFactory mongoDbFactory() throws Exception {
		return new SimpleMongoDbFactory(mongo(), environment.getProperty("mongodb.database"));
	}

	//@Override
	@Bean(name = "mongoTemplate")
	public MongoTemplate mongoTemplate() throws Exception {
		
		log.debug("setup mongoTemplate");
		
		MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(
		        mongoDbFactory()), new MongoMappingContext());
		    // remove _class
		//converter.setTypeMapper(new DefaultMongoTypeMapper(null));
		    
		converter.setCustomConversions(customConversions());
		converter.afterPropertiesSet();


		return new MongoTemplate(mongoDbFactory(), converter);
	}

	@Bean
	//@Override
	public CustomConversions customConversions() {
		
		log.debug("setup customConversions");

		List<Converter<?, ?>> converters = new ArrayList<Converter<?, ?>>();
		converters.addAll(Jsr310Converters.getConvertersToRegister());
		converters.add(new OffsetDateTimeToStringConverter());
		converters.add(new StringToOffsetDateTimeConverter());

		return new CustomConversions(converters);
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	@Bean
	public DateCreatorMongoEventListener dateCreatorMongoEventListener() {
		return new DateCreatorMongoEventListener();
	}

	//@Override
	public String getDatabaseName() {
		return Optional.ofNullable(environment.getProperty("mongodb.database")).orElse("test");
	}

}
