package com.ap.config.xtream;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import org.apache.commons.lang.StringUtils;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class XtreamLocalDateTimeConverter implements SingleValueConverter {
	
	private static final DateTimeFormatter ISO_DATE_OPTIONAL_TIME;
	
	static {
        ISO_DATE_OPTIONAL_TIME = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ISO_LOCAL_DATE)
            .optionalStart()
            .appendLiteral('T')
            .append(DateTimeFormatter.ISO_OFFSET_TIME)
            .toFormatter();
    }


	@Override
	public boolean canConvert(Class arg0) {
		// TODO Auto-generated method stub
		return arg0.equals(OffsetDateTime.class);
	}

	@Override
	public Object fromString(String arg0) {
		// TODO Auto-generated method stub
		if (StringUtils.isBlank(arg0)) {
			throw new IllegalArgumentException("The specified OffsetDateTime string cannot be null");
		} else {
			return OffsetDateTime.parse(arg0, ISO_DATE_OPTIONAL_TIME);
		}
	}

	@Override
	public String toString(Object arg0) {
		// TODO Auto-generated method stub
		return arg0.toString();
	}
}


