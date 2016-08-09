package com.salestock.domain.write.product;

public class ProductException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;

	public ProductException(String message) {
		super(message);
		this.message = message;
	}
}
