package com.ap.config.axon.util.correlationevent;

import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.domain.EventMessage;
import org.axonframework.unitofwork.UnitOfWork;
import org.axonframework.unitofwork.UnitOfWorkListenerAdapter;

import java.util.Collections;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code UnitOfWorkListener} which adds correlation meta data to registered events. This is used to enable the tracking
 * of results (i.e. events) of certain actions (i.e.  * commands).
 */
public class CorrelationUnitOfWorkListener extends UnitOfWorkListenerAdapter {

    private final CorrelationToken correlationToken;

    /**
     * Creates a {@code CorrelationUnitOfWorkListener} which adds correlation meta data to registered events.
     *
     * @param command the command for which
     * @throws NullPointerException     if {@code command} is {@code null}.
     * @throws IllegalArgumentException if {@code command} does not contain the {@code correlationToken} or if the
     * {@code correlationToken} is not a {@code CorrelationToken}.
     */
    public CorrelationUnitOfWorkListener(CommandMessage<?> command) {
        checkNotNull(command);
        checkArgument(command.getMetaData().containsKey(CorrelationToken.KEY));
        checkArgument(command.getMetaData().get(CorrelationToken.KEY) instanceof CorrelationToken);

        correlationToken = (CorrelationToken) command.getMetaData().get(CorrelationToken.KEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> EventMessage<T> onEventRegistered(UnitOfWork unitOfWork, EventMessage<T> event) {
        return event.andMetaData(Collections.singletonMap(CorrelationToken.KEY, correlationToken));
    }
}
