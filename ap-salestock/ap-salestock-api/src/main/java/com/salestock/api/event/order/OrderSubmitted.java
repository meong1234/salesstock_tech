package com.salestock.api.event.order;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.salestock.api.BaseOrderObject;
import com.salestock.api.dto.order.OrderDetail;
import com.salestock.api.identifier.CouponId;
import com.salestock.api.identifier.OrderId;
import com.salestock.shared.EmailAddress;
import com.salestock.shared.FullName;
import com.salestock.shared.Money;
import com.salestock.shared.PostalAddress;
import com.salestock.shared.Telephone;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonDeserialize(builder = OrderSubmitted.OrderSubmittedBuilder.class)
@Value
public class OrderSubmitted extends BaseOrderObject {
	
	private CouponId couponId;
	
	private List<OrderDetail> detail;
	
	private FullName fullName;
	
	private EmailAddress emailAddress;
	
	private Telephone phoneNumber;
	
	private PostalAddress address; 
	
	private Money basePrice;
	
	private Money discount;
	
	private Money totalPrice;

	@Builder
	public OrderSubmitted(OrderId orderId, CouponId couponId, List<OrderDetail> detail, FullName fullName,
			EmailAddress emailAddress, Telephone phoneNumber, PostalAddress address, Money basePrice, Money discount,
			Money totalPrice) {
		super(orderId);
		this.couponId = couponId;
		this.detail = detail;
		this.fullName = fullName;
		this.emailAddress = emailAddress;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.basePrice = basePrice;
		this.discount = discount;
		this.totalPrice = totalPrice;
	}
	
	@JsonPOJOBuilder(withPrefix = "")
    public static final class OrderSubmittedBuilder {
    }
}
