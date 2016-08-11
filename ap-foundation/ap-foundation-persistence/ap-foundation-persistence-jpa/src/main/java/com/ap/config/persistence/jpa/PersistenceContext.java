package com.ap.config.persistence.jpa;

import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.ap.config.persistence.jpa.base.VersionedEntityChecker;
import com.ap.misc.springutil.ApplicationContextProvider;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

import lombok.extern.slf4j.Slf4j;

@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
@EnableJpaRepositories(value= {"com"}, includeFilters = @ComponentScan.Filter(pattern = "..*JpaRepository", type = FilterType.REGEX))
@EntityScan("com.ap")
@Configuration
@Slf4j
public class PersistenceContext {
	
	@Bean
	public Hibernate4Module hibernate4Module() {
		return new Hibernate4Module();
	}

	@Bean
	public VersionedEntityChecker entityChecker() {
		return new VersionedEntityChecker();
	}

	@Bean
	public ApplicationContextProvider applicationContextProvider() {
		return new ApplicationContextProvider();
	}
	

}
