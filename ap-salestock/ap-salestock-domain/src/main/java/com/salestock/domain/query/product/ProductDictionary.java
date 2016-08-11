package com.salestock.domain.query.product;

import org.springframework.data.mongodb.core.mapping.Document;

import com.ap.config.persistence.nosql.mongo.AbstractDocument;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.salestock.api.identifier.ProductId;
import com.salestock.shared.Money;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Document
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PUBLIC)
@Data
@JsonDeserialize(builder = ProductDictionary.ProductDictionaryBuilder.class)
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class ProductDictionary extends AbstractDocument{

	private String productName;
	
	private int qty;
	
	private Money price;
	
	ProductDictionary(){
	}
	
	public ProductDictionary(ProductId productId, String productName, int startingQty, Money startingPrice) {
		this(productId.getIdentifier(), productName, startingQty, startingPrice, true);		
	}

	@Builder
	public ProductDictionary(String productId, String productName, int startingQty, Money startingPrice, Boolean active) {
		super(productId, active);
		this.productName = productName;
		this.qty = startingQty;
		this.price = startingPrice;
	}
	
	public void opName(int newQty, Money newPrice) {
		this.price = newPrice;
		this.qty = newQty;
	}
	
	public boolean isValid(int qty) {
		return (this.qty >= qty);
	}
	
	@JsonPOJOBuilder(withPrefix = "")
	public static final class CouponDictionaryBuilder {
	}
}
