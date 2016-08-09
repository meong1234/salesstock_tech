package com.ap.config.persistence.nosql.elasticsearch;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.inject.Inject;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@EnableElasticsearchRepositories(value = {
		"com.ap" }, includeFilters = @ComponentScan.Filter(pattern = "..*ElasticRepository", type = FilterType.REGEX))
@Component
@Slf4j
public class ElasticSearchConfiguration {

	
	@Inject
	private Environment environment;

	@Bean
	public EntityMapper entityMapper(ObjectMapper objectMapper) {
		return new CustomEntityMapper(objectMapper);
	}

	@Bean
	public ElasticsearchTemplate elasticsearchTemplate(Client client, EntityMapper entityMapper) {
		log.debug("setup elasticsearchTemplate");
		return new ElasticsearchTemplate(client, entityMapper);
	}

	@Bean
	public Client client() {
		log.debug("setup elasticsearchclient");
		Settings.Builder settings = Settings.builder().put("cluster.name", environment.getProperty("spring.data.elasticsearch.cluster-name"))
				.put("cluster.routing.allocation.enable", "all")
				.put("cluster.routing.allocation.allow_rebalance", "true");

		InetAddress address = null;
		try {
			address = InetAddress.getByName(environment.getProperty("spring.data.elasticsearch.cluster-host"));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// create a client
		Client elasticsearchClient = TransportClient.builder().settings(settings.build()).build()
				.addTransportAddress(new InetSocketTransportAddress(address, environment.getProperty("spring.data.elasticsearch.cluster-port", int.class)));

		
		log.debug("end client {}", elasticsearchClient);
		return elasticsearchClient;
	}

}
