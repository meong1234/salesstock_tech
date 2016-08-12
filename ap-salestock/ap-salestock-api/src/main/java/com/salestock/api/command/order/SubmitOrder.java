package com.salestock.api.command.order;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.axonframework.common.Assert;

import com.ap.misc.util.ValidatorUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.salestock.api.dto.order.OrderDetailDto;
import com.salestock.api.identifier.CouponId;
import com.salestock.shared.EmailAddress;
import com.salestock.shared.FullName;
import com.salestock.shared.PostalAddress;
import com.salestock.shared.Telephone;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@EqualsAndHashCode
@ToString
@JsonDeserialize(builder = SubmitOrder.SubmitOrderBuilder.class)
@Value
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class SubmitOrder {
	
	private CouponId couponId;
	
	private List<OrderDetailDto> detail;
	
	private FullName fullName;
	
	private EmailAddress emailAddress;
	
	private Telephone phoneNumber;
	
	private PostalAddress address;

	@Builder
	public SubmitOrder(CouponId couponId, List<OrderDetailDto> detail, FullName fullName, EmailAddress emailAddress,
			Telephone phoneNumber, PostalAddress address) {

		Assert.notNull(fullName, "fullName can't null");
		Assert.notNull(emailAddress, "emailAddress can't null");
		Assert.notNull(phoneNumber, "phoneNumber can't null");
		Assert.notNull(address, "address can't null");
		Assert.isTrue(ValidatorUtil.isPresent(detail), "product not available");
		Assert.isTrue(isUniqueProduct(detail), "one or more product is not unique");
	
		this.couponId = couponId;
		this.detail = detail;
		this.fullName = fullName;
		this.emailAddress = emailAddress;
		this.phoneNumber = phoneNumber;
		this.address = address;
	}
	
	private static boolean isUniqueProduct(List<OrderDetailDto> orderDto) {
		Map<String, Long> map = orderDto
			.stream()
			.collect(Collectors.groupingBy(p -> p.toString(), Collectors.counting()));
		
		for (Map.Entry<String, Long> entry : map.entrySet()) {
			if (entry.getValue() > 1) {
				return false;
			}
		}
		
		return true;
	}


	@JsonPOJOBuilder(withPrefix = "")
    public static final class SubmitOrderBuilder {
    }
}
