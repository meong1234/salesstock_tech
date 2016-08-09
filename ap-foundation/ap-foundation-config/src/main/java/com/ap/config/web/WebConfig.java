package com.ap.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.DeferredResultProcessingInterceptorAdapter;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
    	
    	log.info("configureAsyncSupport");
    	configurer.setDefaultTimeout(10*60000);
    	
        configurer.registerDeferredResultInterceptors(
                new DeferredResultProcessingInterceptorAdapter() {

                    @Override
                    public <T> boolean handleTimeout(NativeWebRequest request, DeferredResult<T> result) {
                    	log.info("async timeout : "+result.toString());
                        result.setErrorResult(new ServiceUnavailableException());
                        return false;
                    }
                });
    }
}

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
class ServiceUnavailableException extends Exception { }
