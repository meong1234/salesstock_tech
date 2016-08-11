package com.salestock.domain.write.order;

public class OrderException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;

	public OrderException(String message) {
		super(message);
		this.message = message;
	}
}
