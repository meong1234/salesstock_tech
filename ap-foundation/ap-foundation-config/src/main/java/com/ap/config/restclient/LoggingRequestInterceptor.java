package com.ap.config.restclient;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

    	log.debug("HttpRequest \r\n Header:{} \r\n Method:{} \r\n URL:{}", request.getHeaders(), request.getMethod(), request.getURI());
    	log.debug("Body \r\n {}", new String(body, "UTF-8"));
    	
        ClientHttpResponse response = execution.execute(request, body);

        log(request,body,response);

        return response;
    }

    private void log(HttpRequest request, byte[] body, ClientHttpResponse response) throws IOException {
    	StringWriter writer = new StringWriter();
    	IOUtils.copy(response.getBody(), writer, Charset.defaultCharset());
    	String responseString = writer.toString();
        log.debug("ClientHttpResponse \r\n Header:{} \r\n Method:{} \r\n URL:{} \r\n Response:{}", response.getHeaders(), request.getMethod(), request.getURI(), responseString);
    }
}
