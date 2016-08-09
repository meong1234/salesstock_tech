package com.salesstock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;

@EnableAspectJAutoProxy
@SpringBootApplication
@EnableAutoConfiguration(exclude = 	ElasticsearchAutoConfiguration.class)
@ComponentScan(
        excludeFilters = @ComponentScan.Filter(classes = ElasticsearchAutoConfiguration.class, type = FilterType.ASSIGNABLE_TYPE)
)
public class SalesStockMonolithApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalesStockMonolithApplication.class, args);
	}
}
