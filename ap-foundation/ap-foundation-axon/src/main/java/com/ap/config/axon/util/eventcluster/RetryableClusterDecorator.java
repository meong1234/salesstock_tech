package com.ap.config.axon.util.eventcluster;

import java.util.Set;

import org.axonframework.domain.EventMessage;
import org.axonframework.eventhandling.Cluster;
import org.axonframework.eventhandling.ClusterMetaData;
import org.axonframework.eventhandling.EventListener;
import org.axonframework.eventhandling.EventProcessingMonitor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import lombok.extern.slf4j.Slf4j;

/**
 * Cluster decorator that will retry when publishing events fail. The number of retries is set to 3 and will be retried
 * first after 100ms then 1sec and finally 10sec.
 */

@Slf4j
public class RetryableClusterDecorator implements Cluster {

	private final Cluster delegate;

	public RetryableClusterDecorator(Cluster delegate) {
		this.delegate = delegate;
	}

	@Override
	public String getName() {
		return delegate.getName();
	}

	@Override
	@Retryable(maxAttempts = 5, backoff = @Backoff(delay = 100, multiplier = 1.5, maxDelay = 500))
	public void publish(EventMessage... events) {
		delegate.publish(events);
	}

	@Override
	public void subscribe(EventListener eventListener) {
		delegate.subscribe(eventListener);
	}

	@Override
	public void unsubscribe(EventListener eventListener) {
		delegate.unsubscribe(eventListener);
	}

	@Override
	public Set<EventListener> getMembers() {
		return delegate.getMembers();
	}

	@Override
	public ClusterMetaData getMetaData() {
		return delegate.getMetaData();
	}

	@Override
	public void subscribeEventProcessingMonitor(EventProcessingMonitor monitor) {
		delegate.subscribeEventProcessingMonitor(monitor);
	}

	@Override
	public void unsubscribeEventProcessingMonitor(EventProcessingMonitor monitor) {
		delegate.unsubscribeEventProcessingMonitor(monitor);
	}
}
