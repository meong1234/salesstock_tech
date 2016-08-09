package com.ap.config.axon.util.callback;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.ap.config.axon.util.correlationevent.EventCallback;

public abstract class CorrelationEventCallback<T> implements Future<T>, EventCallback {

	private volatile T result = null;
    private volatile boolean cancelled = false;
    private final CountDownLatch countDownLatch;

    public CorrelationEventCallback() {
        countDownLatch = new CountDownLatch(1);
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        countDownLatch.await();
        return result;
    }

    @Override
    public T get(final long timeout, final TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        countDownLatch.await(timeout, unit);
        return result;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public boolean isDone() {
        return countDownLatch.getCount() == 0;
    }

    @Override
    public boolean cancel(final boolean mayInterruptIfRunning) {
        if (isDone()) {
            return false;
        } else {
            countDownLatch.countDown();
            cancelled = true;
            return !isDone();
        }
    }

    public void setResult(T result) {
        this.result = result;
    }

    public void countDownLatch() {
        countDownLatch.countDown();
    }
}
