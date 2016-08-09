  package com.ap.config.axon.util;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.web.context.request.async.DeferredResult;

import com.ap.config.axon.util.callback.DeferredResultCallback;
import com.ap.domain.base.SessionDto;
import com.ap.misc.springutil.SecurityUtils;

public abstract class AbstractCommandSender {

	protected abstract MyCommandGateway commandGateway();

	protected <R> DeferredResult<R> sendFuture(Object command) {

		SessionDto sessionDto = SecurityUtils.getCurrentSessionData();

		DeferredResultCallback<R> callback = new DeferredResultCallback<R>();

		commandGateway().send(command, sessionDto, callback);

		return callback.getFuture();
	}

	protected <R> R sendWait(Object command, long timeout, TimeUnit unit)
			throws TimeoutException, InterruptedException {

		SessionDto sessionDto = SecurityUtils.getCurrentSessionData();

		return commandGateway().sendCommandAndWait(command, sessionDto,timeout,
				unit);
	}
}
