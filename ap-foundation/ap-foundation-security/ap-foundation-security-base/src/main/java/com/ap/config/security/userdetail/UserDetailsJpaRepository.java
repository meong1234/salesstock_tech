package com.ap.config.security.userdetail;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailsJpaRepository extends PagingAndSortingRepository<UserDetail, String> {
	
	Optional<UserDetail> findOneByUsername(String username);
}

