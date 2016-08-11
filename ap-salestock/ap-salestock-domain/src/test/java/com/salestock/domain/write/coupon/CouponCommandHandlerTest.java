package com.salestock.domain.write.coupon;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import org.axonframework.eventsourcing.AggregateFactory;
import org.axonframework.eventsourcing.GenericAggregateFactory;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import com.ap.config.axon.util.AbstractCommandHandler;
import com.ap.test.BaseAggregateTest;
import com.ap.test.BaseMatcher;
import com.salestock.api.command.coupon.DeductCoupon;
import com.salestock.api.command.coupon.RegisterCoupon;
import com.salestock.api.dto.coupon.BaseCoupon;
import com.salestock.api.dto.coupon.NominalCoupon;
import com.salestock.api.event.coupon.CouponDeducted;
import com.salestock.api.event.coupon.CouponError;
import com.salestock.api.event.coupon.CouponRegistered;
import com.salestock.api.identifier.CouponId;
import com.salestock.api.identifier.OrderId;
import com.salestock.shared.Money;

public class CouponCommandHandlerTest extends BaseAggregateTest<CouponAggregate>{
	
	public static CouponId COUPON = new CouponId("couponId");
	
	public static OrderId ORDERID = new OrderId("orderId");
	
	public static BaseCoupon createCoupon() {
		return new NominalCoupon(OffsetDateTime.now(), OffsetDateTime.now().plusDays(2), Money.idrMoney(new BigDecimal(9000)));
	}
	
	public static RegisterCoupon createRegisterCoupon(int qty) {
		return new RegisterCoupon(qty, createCoupon());
	}

	public static CouponRegistered createCouponRegistered(RegisterCoupon cmd) {
		return new CouponRegistered(COUPON, cmd.getStartingQty(), cmd.getInfo());
	}
	
	public static DeductCoupon createDeductCoupon() {
		return new DeductCoupon(COUPON, ORDERID);
	}
	
	public static CouponDeducted createCouponDeducted() {
		return new CouponDeducted(COUPON, ORDERID, 9);
	}

	public static CouponError createCouponError() {
		return new CouponError(COUPON, ORDERID, "error");
	}

	@Test
	public void testOnDeductProduct() {
		DeductCoupon cmd = createDeductCoupon();
		
		fixture
			.given(createCouponRegistered(createRegisterCoupon(10)))
			.when(cmd)
			.expectEventsMatching(
					BaseMatcher.aSequenceOf(anEventLike(createCouponDeducted()))
					);
	}
	
	@Test
	public void testOnFailDeductProduct() {
		DeductCoupon cmd = createDeductCoupon();
		
		fixture
			.given(createCouponRegistered(createRegisterCoupon(1)), createCouponDeducted())
			.when(cmd)
			.expectPublishedEventsMatching(
					BaseMatcher.aSequenceOf(anEventLike(createCouponError()))
					);
	}

	@Test
	public void testOnRegisterCoupon() {
		RegisterCoupon cmd = createRegisterCoupon(10);

		fixture.given().when(cmd)
				.expectEventsMatching(BaseMatcher.aSequenceOf(anEventLike(createCouponRegistered(cmd))));
	}
	
	public static <T> Matcher<CouponRegistered> anEventLike(CouponRegistered event) {
		return new IsSameCouponRegistered(event);
	}

	public static <T> Matcher<CouponError> anEventLike(CouponError event) {
		return new IsSameCouponError(event);
	}

	public static <T> Matcher<CouponDeducted> anEventLike(CouponDeducted event) {
		return new IsSameCouponDeducted(event);
	}
	
	
	public static class IsSameCouponDeducted extends TypeSafeMatcher<CouponDeducted> {
		private CouponDeducted operand;

		public IsSameCouponDeducted(CouponDeducted operand) {
			this.operand = operand;
		}

		@Override
		public void describeTo(Description description) {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected boolean matchesSafely(CouponDeducted item) {
			//just check qty and productid
			return (item.getCouponId().equals(operand.getCouponId()));
		}
	}

	public static class IsSameCouponError extends TypeSafeMatcher<CouponError> {

		private CouponError operand;

		public IsSameCouponError(CouponError operand) {
			this.operand = operand;
		}

		@Override
		public void describeTo(Description description) {
		}

		@Override
		protected boolean matchesSafely(CouponError item) {
			return item.getCouponId().equals(operand.getCouponId());
		}
	}

	public static class IsSameCouponRegistered extends TypeSafeMatcher<CouponRegistered> {

		private CouponRegistered operand;

		public IsSameCouponRegistered(CouponRegistered operand) {
			this.operand = operand;
		}

		@Override
		public void describeTo(Description description) {
			description.appendValue(operand);
		}

		@Override
		protected boolean matchesSafely(CouponRegistered item) {
			return ((item.getStartingQty() == operand.getStartingQty()));
		}
	}

	@Override
	protected Class<CouponAggregate> getAggregateType() {
		return CouponAggregate.class;
	}

	@Override
	protected AggregateFactory<CouponAggregate> getAggregateFactory() {
		return new GenericAggregateFactory<>(getAggregateType());
	}

	@Override
	protected AbstractCommandHandler<CouponAggregate> getCommandHandler() {
		return new CouponCommandHandler();
	}

	@Override
	protected void prepareOther(AbstractCommandHandler<CouponAggregate> commandHandler) {
		// TODO Auto-generated method stub
	}

}
