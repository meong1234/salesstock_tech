package com.ap.config.persistence.nosql.mongo.jsr310;

import java.time.OffsetDateTime;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

public class OffsetDateTimeToStringConverter implements Converter<OffsetDateTime, String> {

    @Override
    public String convert(OffsetDateTime entityValue) {
        return entityValue != null ? entityValue.toString() : null;

    }
}
