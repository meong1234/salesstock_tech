package com.ap.config.axon.util.correlationevent;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.common.annotation.MetaData;
import org.axonframework.domain.EventMessage;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.annotation.AnnotationEventListenerAdapter;
import org.axonframework.eventhandling.annotation.EventHandler;

import static org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage;

public class EventWaitingGateway {
	
	private CommandBus commandBus;
    private EventBus eventBus;

    private final Map<CorrelationToken, TimedEventCallback> callbacks = new ConcurrentHashMap<>();

    private AtomicBoolean started = new AtomicBoolean(false);

    public void sendAndWaitForEvent(Object command, final EventCallback callback, final long timeoutInMillis) {
        final CorrelationToken correlationToken = new CorrelationToken();

        final TimedEventCallback timedEventCallback = new TimedEventCallback(callback);
        callbacks.put(correlationToken, timedEventCallback);
        timedEventCallback.scheduleTimer(new Runnable() {
            @Override
            public void run() {
                callbacks.remove(correlationToken);
            }
        }, timeoutInMillis);

        if (started.compareAndSet(false, true)) {
            eventBus.subscribe(new AnnotationEventListenerAdapter(this));
        }

        final CommandMessage commandMessage = asCommandMessage(command)
                .andMetaData(Collections.singletonMap(CorrelationToken.KEY, correlationToken));
        commandBus.dispatch(commandMessage);
    }

    @EventHandler
    protected void onEvent(EventMessage<?> message,
                           @MetaData(value = CorrelationToken.KEY, required = true) CorrelationToken correlationToken) {
        final TimedEventCallback timedEventCallback = callbacks.get(correlationToken);
        if (timedEventCallback != null) {
            boolean handled = timedEventCallback.onEvent(message);
            if (handled) {
                timedEventCallback.cancelTimer();
                callbacks.remove(correlationToken);
            }
        }
    }

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void setCommandBus(CommandBus commandBus) {
        this.commandBus = commandBus;
    }

    private static class TimedEventCallback implements EventCallback {

        private final EventCallback callback;
        private final ScheduledExecutorService scheduledExecutorService;

        public TimedEventCallback(EventCallback callback) {
            this.callback = callback;
            this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        }

        public void scheduleTimer(Runnable task, long delayInMillis) {
            this.scheduledExecutorService.schedule(task, delayInMillis, TimeUnit.MILLISECONDS);
        }

        public void cancelTimer() {
            this.scheduledExecutorService.shutdownNow();
        }

        @Override
        public boolean onEvent(EventMessage<?> event) {
            return callback.onEvent(event);
        }
    }

}
