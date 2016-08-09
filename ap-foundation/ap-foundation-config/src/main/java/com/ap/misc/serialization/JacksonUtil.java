package  com.ap.misc.serialization;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JacksonUtil {
	
	public static SimpleModule bigDecimalModule() {
		return new SimpleModule("bigdecimal-module", new Version(1, 0, 0, null, null, null))
                .addDeserializer(BigDecimal.class, new BigDecimalMoneyDeserializer())
                .addSerializer(BigDecimal.class, new BigDecimalSerialization());
	}

	public static JavaTimeModule jsrModule() {
		JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(OffsetDateTime.class, JSR310ZonedTimeSerializer.INSTANCE);
        module.addSerializer(ZonedDateTime.class, JSR310ZonedTimeSerializer.INSTANCE);
        module.addSerializer(LocalDateTime.class, JSR310DateTimeSerializer.INSTANCE);
        module.addSerializer(Instant.class, JSR310DateTimeSerializer.INSTANCE);
        module.addDeserializer(LocalDate.class, JSR310LocalDateDeserializer.INSTANCE);
        return module;
	}
	
	public static SimpleModule jodaModule() {
		SimpleModule module = new SimpleModule("jodaModule");
        module.addSerializer(org.joda.time.DateTime.class, new JodaDateTimeSerializer());
        module.addDeserializer(org.joda.time.DateTime.class, new JodaDateTimeDeserializer());
        module.addSerializer(org.joda.time.LocalDate.class, new JodaLocalDateSerializer());
        module.addDeserializer(org.joda.time.LocalDate.class, new ISO8601LocalDateDeserializer());
        return module;
	}
}
