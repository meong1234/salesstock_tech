package com.salestock.api;

import com.salestock.api.identifier.ProductId;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Getter
public abstract class BaseProductObject {

	private ProductId productId;
}
