package com.ap.config.jackson;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.ap.misc.serialization.JacksonUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
public class JacksonConfiguration {
	
	@Bean
    public ObjectMapper objectMapper() {
		return jackson2ObjectMapperBuilder().build();
	}

    @Bean
    Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
       
        return new Jackson2ObjectMapperBuilder()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .featuresToDisable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
                .findModulesViaServiceLoader(true)
                .modulesToInstall(JacksonUtil.jodaModule())
                .modulesToInstall(JacksonUtil.jsrModule())
                .modulesToInstall(JacksonUtil.bigDecimalModule());
    }
}
