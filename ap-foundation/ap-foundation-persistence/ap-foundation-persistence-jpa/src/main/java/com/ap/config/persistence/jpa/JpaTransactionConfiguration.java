package com.ap.config.persistence.jpa;

import javax.persistence.EntityManagerFactory;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.ap.config.persistence.TransactionConfiguration;

import lombok.extern.slf4j.Slf4j;

@AutoConfigureBefore(TransactionConfiguration.class)
@EnableTransactionManagement()
@Configuration
@Slf4j
public class JpaTransactionConfiguration {
	
	@Bean
	@ConditionalOnMissingBean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		log.debug("platform transaction Manager : JPA");
        return new JpaTransactionManager(entityManagerFactory);
    }

}
