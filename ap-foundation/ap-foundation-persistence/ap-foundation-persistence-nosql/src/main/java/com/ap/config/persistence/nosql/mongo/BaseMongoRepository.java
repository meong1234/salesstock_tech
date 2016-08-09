package com.ap.config.persistence.nosql.mongo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface BaseMongoRepository<T extends AbstractDocument, ID extends Serializable> extends PagingAndSortingRepository<T, ID> {

	List<T> findByLastUpdateAfter(LocalDateTime lastUpdate);
}
