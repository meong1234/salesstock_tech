package com.ap.config.axon.util.correlationevent;

import java.util.Collections;

import org.axonframework.commandhandling.CommandHandlerInterceptor;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.InterceptorChain;
import org.axonframework.unitofwork.UnitOfWork;
import org.axonframework.unitofwork.UnitOfWorkListener;

import lombok.extern.slf4j.Slf4j;

/**
 * {@code CommandHandlerInterceptor} which ensures resulting events will contain the same {@code CorrelationToken} as
 * the command being handled. This is used to enable the tracking of results (i.e. events) of certain actions (i.e.
 * commands).
 */
public class CorrelationCommandHandlerInterceptor implements CommandHandlerInterceptor {

    /**
     * Handler method which registers a {@link CorrelationUnitOfWorkListener} if the given command contains the required
     * meta data to correlate this command to the resulting events.
     *
     * @throws Throwable no checked exceptions will be thrown. This is required to implement the {@code CommandHandlerInterceptor} interface.
     */
    @Override
    public Object handle(CommandMessage<?> command, UnitOfWork unitOfWork, InterceptorChain chain) throws Throwable {
        CommandMessage<?> commandWithKeyAdded = null;
        if (!command.getMetaData().containsKey(CorrelationToken.KEY)) {
            commandWithKeyAdded = command.andMetaData(Collections.singletonMap(CorrelationToken.KEY, new CorrelationToken()));
        }
        UnitOfWorkListener listener = new CorrelationUnitOfWorkListener(commandWithKeyAdded != null ? commandWithKeyAdded : command);
        unitOfWork.registerListener(listener);
        return chain.proceed();
    }
}