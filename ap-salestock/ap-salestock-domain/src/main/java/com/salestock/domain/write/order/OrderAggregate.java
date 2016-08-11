package com.salestock.domain.write.order;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;

import com.salestock.api.command.order.SubmitOrder;
import com.salestock.api.dto.order.OrderDetail;
import com.salestock.api.dto.order.OrderStatus;
import com.salestock.api.event.order.OrderSubmitted;
import com.salestock.api.identifier.OrderId;
import com.salestock.domain.query.coupon.CouponDictionary;
import com.salestock.shared.Money;

public class OrderAggregate extends AbstractAnnotatedAggregateRoot<OrderId> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@AggregateIdentifier
	private OrderId orderId;

	private OrderStatus orderStatus;

	OrderAggregate() {
	}

	/**
	 * submit new order.
	 */
	public OrderAggregate(SubmitOrder cmd, Optional<CouponDictionary> coupon, List<OrderDetail> orderDetail) {

		// calculating baseprice
		Money basePrice = Money.idrMoney(BigDecimal.ZERO);
		for (OrderDetail detail : orderDetail) {
			basePrice = basePrice.add(detail.getTotalPrice());
		}

		// calculated discount
		Money discount = Money.idrMoney(BigDecimal.ZERO);
		if (coupon.isPresent()) {
			coupon.get().getCouponInfo().calculateDiscount(basePrice);
		}

		// calculate total price (base price - discount)
		Money totalPrice = basePrice.dec(discount);
		
		apply(new OrderSubmitted(new OrderId(),
				cmd.getCouponId(), 
				Collections.unmodifiableList(orderDetail), 
				cmd.getFullName(), 
				cmd.getEmailAddress(), 
				cmd.getPhoneNumber(), 
				cmd.getAddress(), 
				basePrice, 
				discount, 
				totalPrice));
	}
	
	@EventHandler
	public void onOrderSubmitted(OrderSubmitted event) {
		this.orderId = event.getOrderId();
		this.orderStatus = OrderStatus.ORDER_SUBMITTED;
	}

}
