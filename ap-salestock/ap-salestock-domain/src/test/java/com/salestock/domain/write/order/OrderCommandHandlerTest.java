package com.salestock.domain.write.order;

import static org.mockito.Matchers.any;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.axonframework.eventsourcing.AggregateFactory;
import org.axonframework.eventsourcing.GenericAggregateFactory;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.ap.config.axon.util.AbstractCommandHandler;
import com.ap.test.BaseAggregateTest;
import com.ap.test.BaseMatcher;
import com.salestock.api.command.order.SubmitOrder;
import com.salestock.api.dto.coupon.NominalCoupon;
import com.salestock.api.dto.order.OrderDetail;
import com.salestock.api.dto.order.OrderDetailDto;
import com.salestock.api.event.order.OrderSubmitted;
import com.salestock.domain.query.coupon.CouponDictionary;
import com.salestock.domain.query.coupon.CouponService;
import com.salestock.domain.query.product.ProductService;
import com.salestock.domain.write.coupon.CouponCommandHandlerTest;
import com.salestock.domain.write.product.ProductCommandHandlerTest;
import com.salestock.shared.Address;
import com.salestock.shared.EmailAddress;
import com.salestock.shared.FullName;
import com.salestock.shared.Money;
import com.salestock.shared.PostalAddress;
import com.salestock.shared.Telephone;

public class OrderCommandHandlerTest  extends BaseAggregateTest<OrderAggregate>{
	
	@Mock
	private CouponService couponService;
	
	@Mock
	private ProductService productService;
	
	public static SubmitOrder createOrder() {
		return new SubmitOrder(CouponCommandHandlerTest.COUPON,
				Collections.singletonList(new OrderDetailDto(ProductCommandHandlerTest.PRODUCT, 2)),
				new FullName("MR", "ANDI", "PANGERAN"),
				new EmailAddress("email@yahoo.com"),
				new Telephone("0811811811"),
				new PostalAddress(new Address("pecenongan", "gambir", "jakarta"), "jakarta", "indonesia", "9999")
		);
	}
	
	public static CouponDictionary createCoupon() {
		return new CouponDictionary(CouponCommandHandlerTest.COUPON, 10, 
				new NominalCoupon(OffsetDateTime.now(), OffsetDateTime.now().plusDays(2), Money.idrMoney(new BigDecimal(1000))));
	}
	
	public static OrderSubmitted createOrderSubmitted(SubmitOrder cmd) {		
		return new OrderSubmitted(ProductCommandHandlerTest.ORDERID, CouponCommandHandlerTest.COUPON, Collections.emptyList(), 
				cmd.getFullName(), cmd.getEmailAddress(), cmd.getPhoneNumber(), cmd.getAddress(),
				Money.idrMoney(new BigDecimal(1000)), Money.idrMoney(new BigDecimal(1000)), Money.idrMoney(new BigDecimal(1000))
				);
	}
	
	public static List<OrderDetail> createOrderDetails() {
		return Collections.singletonList(
				new OrderDetail(
						ProductCommandHandlerTest.PRODUCT,
						"TEST", 
						2, 
						Money.idrMoney(new BigDecimal(100))
				)
		);
	}

	@Test
	public void testOnSubmitOrder() {
		SubmitOrder cmd = createOrder();
		
		//mockup product always valid
		Mockito
			.when(productService.isValidProduct(any(String.class), any(int.class)))
			.thenReturn(true);
		
		//mock products
		Mockito
			.when(productService.convertDetailsDto(any(new ArrayList<OrderDetailDto>().getClass())))
			.thenReturn(createOrderDetails());
		
		//mockup coupon 
		Mockito
			.when(couponService.find(any(String.class)))
			.thenReturn(Optional.of(createCoupon()));
		
		fixture
			.given()
			.when(createOrder())
			.expectEventsMatching(BaseMatcher.aSequenceOf(anEventLike(createOrderSubmitted(cmd))));
	}
	
	public static <T> Matcher<OrderSubmitted> anEventLike(OrderSubmitted event) {
		return new IsSameOrderSubmitted(event);
	}
	
	public static class IsSameOrderSubmitted extends TypeSafeMatcher<OrderSubmitted> {
		private OrderSubmitted operand;

		public IsSameOrderSubmitted(OrderSubmitted operand) {
			this.operand = operand;
		}

		@Override
		public void describeTo(Description description) {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected boolean matchesSafely(OrderSubmitted item) {
			
			System.out.println("operand "+item.toString());
			
			//just check name :D
			return (item.getFullName().equals(operand.getFullName()));
		}
	}

	@Override
	protected Class<OrderAggregate> getAggregateType() {
		return OrderAggregate.class;
	}

	@Override
	protected AggregateFactory<OrderAggregate> getAggregateFactory() {
		return new GenericAggregateFactory<>(getAggregateType());
	}

	@Override
	protected AbstractCommandHandler<OrderAggregate> getCommandHandler() {
		return new OrderCommandHandler();
	}

	@Override
	protected void prepareOther(AbstractCommandHandler<OrderAggregate> commandHandler) {
		
		couponService = Mockito.mock(CouponService.class);
		productService = Mockito.mock(ProductService.class);
		
		((OrderCommandHandler) commandHandler).setCouponService(couponService);
		((OrderCommandHandler) commandHandler).setProductService(productService);
	}

}
