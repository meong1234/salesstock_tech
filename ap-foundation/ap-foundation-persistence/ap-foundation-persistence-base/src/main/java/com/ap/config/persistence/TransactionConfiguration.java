package com.ap.config.persistence;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import lombok.extern.slf4j.Slf4j;

@EnableTransactionManagement
@Configuration
@Slf4j
public class TransactionConfiguration implements TransactionManagementConfigurer {
	
	@Autowired
	private PlatformTransactionManager myTxManager;
	
	@Bean
	@ConditionalOnMissingBean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
		log.debug("platform transaction Manager : Datasource");
		return new DataSourceTransactionManager(dataSource);
    }

	@Override
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		return this.myTxManager;
	}

}
