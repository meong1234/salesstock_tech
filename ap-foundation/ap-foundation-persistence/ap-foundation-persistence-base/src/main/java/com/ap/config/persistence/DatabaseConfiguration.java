package com.ap.config.persistence;

import java.util.Arrays;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import com.ap.config.base.FoundationProperties;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j	
public class DatabaseConfiguration {
	
	@Inject
	private Environment env;

	@Inject
	private FoundationProperties apProperties;
	
	@Bean(destroyMethod = "close")
	public DataSource dataSource(DataSourceProperties dataSourceProperties) {
		log.debug("Configuring Datasource");
		if (dataSourceProperties.getUrl() == null) {
			log.error(
					"Your database connection pool configuration is incorrect! The application"
							+ " cannot start. Please check your Spring profile, current profiles are: {}",
					Arrays.toString(env.getActiveProfiles()));

			throw new ApplicationContextException("Database connection pool is not configured correctly");
		}
		HikariConfig config = new HikariConfig();
		config.setDataSourceClassName(dataSourceProperties.getDriverClassName());
		// config.addDataSourceProperty("url", dataSourceProperties.getUrl());
		if (dataSourceProperties.getUsername() != null) {
			config.addDataSourceProperty("user", dataSourceProperties.getUsername());
		} else {
			config.addDataSourceProperty("user", ""); // HikariCP doesn't allow
														// null user
		}
		if (dataSourceProperties.getPassword() != null) {
			config.addDataSourceProperty("password", dataSourceProperties.getPassword());
		} else {
			config.addDataSourceProperty("password", ""); // HikariCP doesn't
															// allow null
															// password
		}
		// config.addDataSourceProperty("driverType", "thin");
		config.addDataSourceProperty("serverName", apProperties.getDatasource().getServerName());
		config.addDataSourceProperty("portNumber", apProperties.getDatasource().getPortNumber());
		config.addDataSourceProperty("databaseName", apProperties.getDatasource().getDatabaseName());
		
		
//		 * if (metricRegistry != null) {
//		 * config.setMetricRegistry(metricRegistry); }
		 
		
//		if (metricRegistry != null) {
//			config.setMetricRegistry(metricRegistry);
//		}
		
		return new HikariDataSource(config);
	}

}
