package com.ap.config.security.userdetail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ap.config.base.Constants;
import com.ap.config.security.UserNotActivatedException;

import lombok.extern.slf4j.Slf4j;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
@Slf4j
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
	
	@Inject
    private UserDetailsJpaRepository userRepository;
	
	
	@Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {       	
		
		//TODO later change this to use get role, now just role administrator for all :D
		final Collection<GrantedAuthority> grantedAuthorities = Arrays
				.stream(Constants.UserRole.ROLE_ADMINISTRATOR)
				.map(p -> new SimpleGrantedAuthority(p))
				.collect(Collectors.toList());
        

        log.debug("Authenticating {}", login);
        String lowercaseLogin = login.toLowerCase();
        Optional<UserDetail> userFromDatabase = userRepository.findOneByUsername(lowercaseLogin);
        return userFromDatabase.map(user -> {
        	
            if (!user.isActivated()) {
                throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
            }
            //TODO later get from role :D
//            List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
//                    .map(authority -> new SimpleGrantedAuthority(authority.getName()))
//                .collect(Collectors.toList());
            return new org.springframework.security.core.userdetails.User(lowercaseLogin,
                user.getPassword(),
                grantedAuthorities);
        }).orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the " +
        "database"));
    }

}
