package com.ap.config.axon.util.callback;

import org.axonframework.commandhandling.CommandCallback;
import org.springframework.web.context.request.async.DeferredResult;

public class DeferredResultCallback<R> implements CommandCallback<R> {
	
	private final DeferredResult<R> future = new DeferredResult<>();

	@Override
	public void onSuccess(R value) {
		this.future.setResult(value);
		
	}

	@Override
	public void onFailure(Throwable cause) {
		this.future.setErrorResult(cause);
	}
	
	public DeferredResult<R> getFuture() {
        return this.future;
    }

}
