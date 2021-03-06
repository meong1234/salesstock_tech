package com.ap.config.axon.util.eventcluster;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Spring stereotype to be used for entry sql updaters. This will configure the order of processing correctly. Entry updaters
 * will always be processed before view updaters 
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Order(10)
public @interface SyncQueryModelSql {
}

