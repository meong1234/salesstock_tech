package com.ap.config.axon.util.correlationevent;

import org.axonframework.domain.EventMessage;

public interface EventCallback {

    boolean onEvent(EventMessage<?> event);

}