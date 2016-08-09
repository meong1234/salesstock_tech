package com.ap.config.axon.util.eventcluster;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.domain.EventMessage;
import org.axonframework.eventhandling.Cluster;
import org.axonframework.eventhandling.ClusterMetaData;
import org.axonframework.eventhandling.EventListener;
import org.axonframework.eventhandling.EventProcessingMonitor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Slf4j
public class TransactionalClusterDecorator implements Cluster {

    private final Cluster delegate;

    public TransactionalClusterDecorator(Cluster delegate) {
        this.delegate = delegate;
    }


    @Override
    public String getName() {
        return null;
    }

    @Override
    @Transactional(propagation = REQUIRES_NEW)
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

