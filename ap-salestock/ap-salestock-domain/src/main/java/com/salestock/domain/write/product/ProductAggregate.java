package com.salestock.domain.write.product;

import org.axonframework.common.Assert;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;
import org.axonframework.eventsourcing.annotation.EventSourcingHandler;

import com.salestock.api.event.product.ProductOpnamed;
import com.salestock.api.event.product.ProductRegistered;
import com.salestock.api.event.product.ProductSold;
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

	/**
	 * Create / register new product.
	 */
	public ProductAggregate(String productName, int startingQty, Money startingPrice) {
		apply(ProductRegistered.builder().productId(new ProductId()).productName(productName).startingQty(startingQty)
				.startingPrice(startingPrice).build());
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
	 * sell product.
	 * 
	 * @return
	 */
	public void sellProduct(int qty) {		
		Assert.isTrue(this.qty > qty, "not enaough qty for this product "+this.productId.toString());

		apply(ProductSold.builder().productId(this.productId).qty(qty).price(this.price)
				.totalPrice(this.price.multiply(qty)).build());
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
	private void onProductSold(ProductSold event) {
		this.qty -= event.getQty();
	}
}
