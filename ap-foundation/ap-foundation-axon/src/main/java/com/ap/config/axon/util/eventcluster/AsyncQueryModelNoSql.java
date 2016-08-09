package com.ap.config.axon.util.eventcluster;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Spring stereotype to be used for NoSqlView updaters. This will configure the order of processing correctly.
 * NoSqlView updaters will always be processed after entry updaters and sqlview updaters.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Order(10)
public @interface AsyncQueryModelNoSql {
}

