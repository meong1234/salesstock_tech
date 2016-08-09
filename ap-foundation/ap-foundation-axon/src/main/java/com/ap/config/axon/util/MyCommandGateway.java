package com.ap.config.axon.util;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.common.annotation.MetaData;

import com.ap.config.axon.util.correlationevent.CorrelationToken;
import com.ap.domain.base.SessionDto;

public interface MyCommandGateway {
	 <R> void send(Object command, 
			 @MetaData("sessionData") SessionDto session,
			 CommandCallback<R> callback);
	 
	 <R> R send(Object command, 
			 @MetaData("sessionData") SessionDto session);
	 
	 <R> R send(Object command, @MetaData(CorrelationToken.KEY) CorrelationToken correlationToken,
			 @MetaData("sessionData") SessionDto session);
	 
	 <R> R sendCommandAndWait(Object command,
			 @MetaData("sessionData") SessionDto session,
			 long timeout, TimeUnit unit) 
             throws TimeoutException, InterruptedException;
}
