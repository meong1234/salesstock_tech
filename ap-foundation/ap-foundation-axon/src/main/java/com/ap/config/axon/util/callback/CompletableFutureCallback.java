package com.ap.config.axon.util.callback;

import java.util.concurrent.CompletableFuture;

import org.axonframework.commandhandling.CommandCallback;

public class CompletableFutureCallback<R> implements CommandCallback<R> {
	
	private final CompletableFuture<R> future = new CompletableFuture<>();

	@Override
	public void onSuccess(R value) {
		this.future.complete(value);
		
	}

	@Override
	public void onFailure(Throwable cause) {
		this.future.completeExceptionally(cause);		
	}
	
	public CompletableFuture<R> getFuture() {
        return this.future;
    }

}
