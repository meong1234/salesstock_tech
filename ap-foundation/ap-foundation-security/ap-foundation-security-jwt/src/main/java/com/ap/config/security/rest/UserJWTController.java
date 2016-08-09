package com.ap.config.security.rest;

import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ap.config.base.Constants;
import com.ap.config.security.jwt.JWTConfigurer;
import com.ap.config.security.jwt.TokenProvider;
import com.ap.config.security.model.LoginCommand;
import com.ap.config.security.model.SessionInformationDto;
import com.ap.config.security.userdetail.UserDetail;
import com.ap.config.security.userdetail.UserDetailsJpaRepository;
import com.ap.domain.base.SessionDto;
import com.ap.misc.springutil.SecurityUtils;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api", consumes = Constants.V1_0, produces = Constants.V1_0)
@Slf4j
public class UserJWTController {

	@Inject
	private TokenProvider tokenProvider;

	@Inject
	private AuthenticationManager authenticationManager;

	@Inject
	private PasswordEncoder passwordEncoder;

	@Inject
	private UserDetailsJpaRepository userRepository;

	// @RequestMapping(value = "/authenticate", method = RequestMethod.POST)

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> authorize(
			@Valid @RequestBody LoginCommand loginCommand,
			HttpServletResponse response) {

		log.debug("authorize with {} - {}", loginCommand.toString(),
				passwordEncoder.encode(loginCommand.getPassword()));

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				loginCommand.getUsername(), passwordEncoder.encode(loginCommand.getPassword()));

		try {
			UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) this.authenticationManager
					.authenticate(authenticationToken);
			Optional<UserDetail> user = userRepository.findOneByUsername(loginCommand.getUsername().toLowerCase());
			SessionInformationDto session = null;
			if (user.isPresent()) {
				
				SessionDto data = user.get().toSessionDto();
				data.setRemoteAddress(SecurityUtils.getCurrentIp());
				authentication.setDetails(data);

				SecurityContextHolder.getContext().setAuthentication(authentication);
				String jwt = tokenProvider.createToken(authentication, loginCommand.getRememberMe());
				response.addHeader(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);

				session = new SessionInformationDto(jwt, user.get().toUserInformationDto());
			}

			return ResponseEntity.ok(session);
		} catch (AuthenticationException exception) {
			return new ResponseEntity<>(exception.getLocalizedMessage(), HttpStatus.UNAUTHORIZED);
		}
	}

}
