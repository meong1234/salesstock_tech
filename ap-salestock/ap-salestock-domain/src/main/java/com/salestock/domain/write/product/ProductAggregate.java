package com.salestock.domain.write.product;

import java.math.BigDecimal;

import org.axonframework.common.Assert;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;
import org.axonframework.eventsourcing.annotation.EventSourcingHandler;

import com.salestock.api.command.product.RegisterProduct;
import com.salestock.api.event.product.ProductDeducted;
import com.salestock.api.event.product.ProductOpnamed;
import com.salestock.api.event.product.ProductRegistered;
import com.salestock.api.event.product.ProductRetured;
import com.salestock.api.identifier.OrderId;
import com.salestock.api.identifier.ProductId;
import com.salestock.shared.Money;

/**
 * @author Lenovo
 *
 */
public class ProductAggregate extends AbstractAnnotatedAggregateRoot<ProductId> {

	@AggregateIdentifier
	private ProductId productId;

	private int qty;

	private Money price;
	
	ProductAggregate() {
	}

	/**
	 * Create / register new product.
	 */
	public ProductAggregate(RegisterProduct cmd) {
		apply(ProductRegistered
				.builder()
				.productId(new ProductId())
				.productName(cmd.getProductName())
				.startingQty(cmd.getStartingQty())
				.startingPrice(cmd.getStartingPrice())
				.build()
		);
	}

	/**
	 * adjust/opname product.
	 * 
	 * @return
	 */
	public void opnameProduct(int newQty, Money newPrice) {
		apply(ProductOpnamed.builder().productId(this.productId).newQty(newQty).newPrice(newPrice).build());
	}

	/**
	 * deduct product.
	 * 
	 * @return
	 */
	public void deductProduct(OrderId orderId, int qty) {		
		Assert.isTrue(this.qty > qty, "not enaough qty for this product "+this.productId.toString());

		apply(ProductDeducted
				.builder()
				.productId(this.productId)
				.orderId(orderId)
				.qty(qty)
				.price(this.price)
				.totalPrice(this.price.multiply(new BigDecimal(qty)))
				.endQty(this.qty - qty)
				.build());
	}
	
	/**
	 * retur product.
	 * 
	 * @return
	 */
	public void returProduct(int qty) {		
		apply(new ProductRetured(this.productId, qty, this.qty+qty));
	}

	@EventSourcingHandler
	private void onProductRegistered(ProductRegistered event) {
		this.productId = event.getProductId();
		this.qty = event.getStartingQty();
		this.price = event.getStartingPrice();
	}

	@EventSourcingHandler
	private void onProductOpnamed(ProductOpnamed event) {
		this.qty = event.getNewQty();
		this.price = event.getNewPrice();
	}
	
	@EventSourcingHandler
	private void onProductSold(ProductDeducted event) {
		this.qty -= event.getQty();
	}
	
	@EventSourcingHandler
	private void onProductRetured(ProductRetured event) {
		this.qty += event.getReturQty();
	}
}
