package com.salestock.domain.write.coupon;

public class CouponException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;

	public CouponException(String message) {
		super(message);
		this.message = message;
	}
}

