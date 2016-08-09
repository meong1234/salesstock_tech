package com.ap.config.security.jwt;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.ap.config.base.FoundationProperties;
import com.ap.domain.base.SessionDto;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class TokenProvider {
	
	private static final String AUTHORITIES_KEY = "auth";
	private static final String AUTHORITIES_DETAILS = "detail";

    private String secretKey;

    private long tokenValidityInSeconds;

    private long tokenValidityInSecondsForRememberMe;
    
    @Inject
    private FoundationProperties props;
    
    @Inject
    private ObjectMapper jacksonObjectMapper;
    
    @PostConstruct
    public void init() {
        this.secretKey =
        		props.getSecurity().getAuthentication().getJwt().getSecret();

        this.tokenValidityInSeconds =
            1000 * props.getSecurity().getAuthentication().getJwt().getTokenValidityInSeconds();
        this.tokenValidityInSecondsForRememberMe =
            1000 * props.getSecurity().getAuthentication().getJwt().getTokenValidityInSecondsForRememberMe();
    }
    
    public String createToken(Authentication authentication, Boolean rememberMe) {
        String authorities = authentication.getAuthorities().stream()
            .map(authority -> authority.getAuthority())
            .collect(Collectors.joining(","));
        
        SessionDto sessionData = (SessionDto) authentication.getDetails();
        String sessionString = "";
		try {
			sessionString = jacksonObjectMapper.writeValueAsString(sessionData);
		} catch (JsonProcessingException e) {
			log.info("session JsonProcessingException: " + e.getMessage());
		}

        long now = (new Date()).getTime();
        Date validity = new Date(now);
        if (rememberMe) {
            validity = new Date(now + this.tokenValidityInSecondsForRememberMe);
        } else {
            validity = new Date(now + this.tokenValidityInSeconds);
        }

        return Jwts.builder()
            .setSubject(authentication.getName())
            .claim(AUTHORITIES_KEY, authorities)
            .claim(AUTHORITIES_DETAILS, sessionString)
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .setExpiration(validity)
            .compact();
    }

    public Authentication getAuthentication(String token, HttpServletRequest servletRequest) {
        Claims claims = Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody();
        String principal = claims.getSubject();
        
        String sessionString = claims.get(AUTHORITIES_DETAILS).toString();
        SessionDto sessionData = null;
        
        try {
			sessionData = jacksonObjectMapper.readValue(sessionString, SessionDto.class);
		} catch (JsonParseException e) {
			log.info("session JsonParseException: " + e.getMessage());
		} catch (JsonMappingException e) {
			log.info("session JsonParseException: " + e.getMessage());
		} catch (IOException e) {
			log.info("session IOException: " + e.getMessage());
		}
        
        if (sessionData != null) {
        	
        	String ipAddress = servletRequest.getHeader("X-FORWARDED-FOR");  
        	   if (ipAddress == null) {  
        		   ipAddress = servletRequest.getRemoteAddr();  
        	   }
        	   
        	sessionData.setRemoteAddress(ipAddress);
        }

        Collection<? extends GrantedAuthority> authorities =
            Arrays.asList(claims.get(AUTHORITIES_KEY).toString().split(",")).stream()
                .map(authority -> new SimpleGrantedAuthority(authority))
                .collect(Collectors.toList());
        
        UsernamePasswordAuthenticationToken auth =  new UsernamePasswordAuthenticationToken(principal, "", authorities);
        auth.setDetails(sessionData);

        return auth;
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.info("Invalid JWT signature: " + e.getMessage());
            return false;
        }
    }

}
