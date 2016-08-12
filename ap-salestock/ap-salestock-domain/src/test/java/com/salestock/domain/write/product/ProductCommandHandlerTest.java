package com.salestock.domain.write.product;

import java.math.BigDecimal;

import org.axonframework.eventsourcing.AggregateFactory;
import org.axonframework.eventsourcing.GenericAggregateFactory;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import com.ap.config.axon.util.AbstractCommandHandler;
import com.ap.test.BaseAggregateTest;
import com.ap.test.BaseMatcher;
import com.salestock.api.command.product.DeductProduct;
import com.salestock.api.command.product.OpnameProduct;
import com.salestock.api.command.product.RegisterProduct;
import com.salestock.api.event.product.ProductDeducted;
import com.salestock.api.event.product.ProductError;
import com.salestock.api.event.product.ProductOpnamed;
import com.salestock.api.event.product.ProductRegistered;
import com.salestock.api.identifier.OrderId;
import com.salestock.api.identifier.ProductId;
import com.salestock.shared.Money;

public class ProductCommandHandlerTest extends BaseAggregateTest<ProductAggregate> {

	public static ProductId PRODUCT = new ProductId("productId");
	
	public static OrderId ORDERID = new OrderId("orderId");

	public static RegisterProduct createRegisterProduct() {
		return new RegisterProduct("BAJU BARU", 10, Money.idrMoney(new BigDecimal(2000)));
	}

	public static ProductRegistered createProductRegistered(RegisterProduct cmd) {
		return new ProductRegistered(PRODUCT, cmd.getProductName(), cmd.getStartingQty(), cmd.getStartingPrice());
	}
	
	public static OpnameProduct createOpnameProduct() {
		return new OpnameProduct(PRODUCT, 20, Money.idrMoney(new BigDecimal(2000)));
	}
	
	public static ProductOpnamed createProductOpnamed(OpnameProduct cmd) {
		return new ProductOpnamed(cmd.getProductId(), cmd.getNewQty(), cmd.getNewPrice());
	}
	
	public static DeductProduct createDeductProduct(int qty) {
		return new DeductProduct(PRODUCT, ORDERID, qty);
	}
	
	public static ProductDeducted createProductDeducted() {
		return new ProductDeducted(PRODUCT, ORDERID, 5,  Money.idrMoney(new BigDecimal(2000)), Money.idrMoney(new BigDecimal(2000)), 5);
	}

	public static ProductError createProductError() {
		return new ProductError(PRODUCT, ORDERID, "error");
	}

	@Test
	public void testOnRegisterProduct() {

		RegisterProduct cmd = createRegisterProduct();

		fixture.given().when(cmd)
				.expectEventsMatching(BaseMatcher.aSequenceOf(anEventLike(createProductRegistered(cmd))));
	}

	@Test
	public void testOnOpnameProduct() {
		OpnameProduct cmd = createOpnameProduct();

		fixture
			.given(createProductRegistered(createRegisterProduct()))
			.when(cmd)
			.expectEventsMatching(
				BaseMatcher.aSequenceOf(anEventLike(createProductOpnamed(cmd)))
			);
	}

	@Test
	public void testOnDeductProduct() {
		DeductProduct cmd = createDeductProduct(5);
		
		fixture
			.given(createProductRegistered(createRegisterProduct()))
			.when(cmd)
			.expectEventsMatching(
					BaseMatcher.aSequenceOf(anEventLike(createProductDeducted()))
					);
	}
	
	@Test
	public void testOnFailDeductProduct() {
		DeductProduct cmd = createDeductProduct(300);
		
		fixture
			.given(createProductRegistered(createRegisterProduct()))
			.when(cmd)
			.expectPublishedEventsMatching(
					BaseMatcher.aSequenceOf(anEventLike(createProductError()))
					);
	}

	public static <T> Matcher<ProductRegistered> anEventLike(ProductRegistered event) {
		return new IsSameProductRegistered(event);
	}

	public static <T> Matcher<ProductOpnamed> anEventLike(ProductOpnamed event) {
		return new IsSameProductOpnamed(event);
	}

	public static <T> Matcher<ProductError> anEventLike(ProductError event) {
		return new IsSameProductError(event);
	}

	public static <T> Matcher<ProductDeducted> anEventLike(ProductDeducted event) {
		return new IsSameProductDeducted(event);
	}
	
	
	public static class IsSameProductDeducted extends TypeSafeMatcher<ProductDeducted> {
		private ProductDeducted operand;

		public IsSameProductDeducted(ProductDeducted operand) {
			this.operand = operand;
		}

		@Override
		public void describeTo(Description description) {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected boolean matchesSafely(ProductDeducted item) {
			//just check qty and productid
			return (item.getProductId().equals(operand.getProductId()) &&
					item.getQty() == operand.getQty());
		}
	}
	
	public static class IsSameProductOpnamed extends TypeSafeMatcher<ProductOpnamed> {

		private ProductOpnamed operand;

		public IsSameProductOpnamed(ProductOpnamed operand) {
			this.operand = operand;
		}

		@Override
		public void describeTo(Description description) {
		}

		@Override
		protected boolean matchesSafely(ProductOpnamed item) {
			return item.getProductId().equals(operand.getProductId());
		}
	}

	public static class IsSameProductError extends TypeSafeMatcher<ProductError> {

		private ProductError operand;

		public IsSameProductError(ProductError operand) {
			this.operand = operand;
		}

		@Override
		public void describeTo(Description description) {
		}

		@Override
		protected boolean matchesSafely(ProductError item) {
			return item.getProductId().equals(operand.getProductId());
		}
	}

	public static class IsSameProductRegistered extends TypeSafeMatcher<ProductRegistered> {

		private ProductRegistered operand;

		public IsSameProductRegistered(ProductRegistered operand) {
			this.operand = operand;
		}

		@Override
		public void describeTo(Description description) {
			description.appendValue(operand);
		}

		@Override
		protected boolean matchesSafely(ProductRegistered item) {
			return (item.getProductName().equals(operand.getProductName()))
					&& (item.getStartingQty() == operand.getStartingQty())
					&& (item.getStartingPrice().equals(operand.getStartingPrice()));
		}
	}

	@Override
	protected Class<ProductAggregate> getAggregateType() {
		return ProductAggregate.class;
	}

	@Override
	protected AggregateFactory<ProductAggregate> getAggregateFactory() {
		return new GenericAggregateFactory<>(getAggregateType());
	}

	@Override
	protected AbstractCommandHandler<ProductAggregate> getCommandHandler() {
		return new ProductCommandHandler();
	}

	@Override
	protected void prepareOther(AbstractCommandHandler<ProductAggregate> commandHandler) {
		// TODO Auto-generated method stub
		
	}

}
