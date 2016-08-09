package com.ap.config.xtream;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import org.apache.commons.lang.StringUtils;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class XtreamLocalDateConverter implements SingleValueConverter {

	private static final DateTimeFormatter ISO_DATE_OPTIONAL_TIME;

	static {
		ISO_DATE_OPTIONAL_TIME = new DateTimeFormatterBuilder().append(DateTimeFormatter.ISO_LOCAL_DATE).optionalStart()
				.appendLiteral('T').append(DateTimeFormatter.ISO_OFFSET_TIME).toFormatter();
	}

	@Override
	public boolean canConvert(Class type) {
		return type.equals(LocalDate.class);
	}

	@Override
	public Object fromString(String str) {
		if (StringUtils.isBlank(str)) {
			throw new IllegalArgumentException("The specified LocalDate string cannot be null");
		} else {
			return LocalDate.parse(str, ISO_DATE_OPTIONAL_TIME);
		}
	}

	@Override
	public String toString(Object arg0) {
		return arg0.toString();
	}

}
