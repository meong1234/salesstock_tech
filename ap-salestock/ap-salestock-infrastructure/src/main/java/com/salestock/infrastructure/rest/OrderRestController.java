package com.salestock.infrastructure.rest;

import javax.validation.Valid;

import org.axonframework.common.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.ap.config.axon.util.AbstractCommandSender;
import com.ap.config.axon.util.MyCommandGateway;
import com.ap.config.base.Constants;
import com.salestock.api.command.order.SubmitOrder;

@RestController
@RequestMapping("/api/order")
public class OrderRestController extends AbstractCommandSender{

	private final MyCommandGateway commandGateway;
	
	@Autowired
	public OrderRestController(MyCommandGateway myCommandGateway) {
		Assert.notNull(myCommandGateway, "myCommandGateway can't null");
		this.commandGateway = myCommandGateway;
	}
	
	@RequestMapping(value = "/submitorder", method = RequestMethod.POST, consumes = Constants.V1_0, produces = Constants.V1_0)
	public DeferredResult<Object> getAvailibility(@Valid @RequestBody SubmitOrder request) {
		return this.sendFuture(request);
	}

	@Override
	protected MyCommandGateway commandGateway() {
		return this.commandGateway;
	}
}
