package com.ap.config.persistence.nosql.mongo.jsr310;

import java.time.OffsetDateTime;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

public class StringToOffsetDateTimeConverter implements Converter<String, OffsetDateTime> {

    @Override
    public OffsetDateTime convert(String databaseValue) {
        return databaseValue != null ? OffsetDateTime.parse(databaseValue) : null;

    }

}
