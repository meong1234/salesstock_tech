package com.salestock.domain.query.product;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.axonframework.common.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salestock.api.dto.order.OrderDetail;
import com.salestock.api.dto.order.OrderDetailDto;
import com.salestock.domain.write.product.ProductException;

@Service
public class ProductService {
	
	private ProductDictionaryMongoRepository repository;

	@Autowired
	public ProductService(ProductDictionaryMongoRepository repository) {
		Assert.notNull(repository, "repository can't null");
		this.repository = repository;
	}
	
	public Optional<ProductDictionary> find(String arg0) {
		return Optional.of(repository.findOne(arg0));
	}
	
	public boolean isValidProduct(String arg0, int qty) {
		Optional<ProductDictionary> product = find(arg0);
		if (product.isPresent()) {
			return product.get().isValid(qty);
		}
		return false;
	}
	
	public List<OrderDetail> convertDetailsDto(List<OrderDetailDto> detailsDto) {
		return detailsDto
				.stream()
				.map(dto -> {
					ProductDictionary dictionary = find(dto.getProductId().getIdentifier())
					.orElseThrow(() -> new ProductException("product not found for productid " +dto.getProductId().getIdentifier()));
					
					return new OrderDetail(dto.getProductId(), dictionary.getProductName(), dto.getQty(), dictionary.getPrice());
				}).collect(Collectors.toList());
	}

}
