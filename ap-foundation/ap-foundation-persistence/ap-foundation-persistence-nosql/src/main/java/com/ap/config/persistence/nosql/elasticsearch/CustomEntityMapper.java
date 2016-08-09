package com.ap.config.persistence.nosql.elasticsearch;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.EntityMapper;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomEntityMapper implements EntityMapper {
	
	private ObjectMapper objectMapper;
	 
	
	@Autowired
	public CustomEntityMapper(ObjectMapper objectMapper) {
		super();
		this.objectMapper = objectMapper;
	}

	@Override
	public String mapToString(Object object) throws IOException {
		return objectMapper.writeValueAsString(object);
	}

	@Override
	public <T> T mapToObject(String source, Class<T> clazz) throws IOException {
		return objectMapper.readValue(source, clazz);
	}

}
