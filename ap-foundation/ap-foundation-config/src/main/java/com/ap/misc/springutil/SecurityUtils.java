package com.ap.misc.springutil;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ap.config.base.Constants;
import com.ap.domain.base.SessionDto;
import com.ap.domain.base.TenantId;
import com.ap.domain.base.UserId;

public class SecurityUtils {
	private SecurityUtils() {
	}

	public static String getCurrentIp() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		
		if (request != null) {
			String ipAddress = request.getHeader("X-FORWARDED-FOR");
			if (ipAddress == null) {
				ipAddress = request.getRemoteAddr();
			}
			return ipAddress;
		}

		throw new IllegalStateException("ServletRequestAttributes not found!");
	}

	/**
	 * Get the login of the current user.
	 */
	public static String getCurrentUserLogin() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		String userName = null;
		if (authentication != null) {
			if (authentication.getPrincipal() instanceof UserDetails) {
				UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
				userName = springSecurityUser.getUsername();
			} else if (authentication.getPrincipal() instanceof String) {
				userName = (String) authentication.getPrincipal();
			}
		}
		return (userName != null ? userName : Constants.DEFAULT_USER);
	}

	/**
	 * Get the login of the current user.
	 */
	public static SessionDto getCurrentSessionData() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		SessionDto sessionData = null;
		if (authentication != null) {
			sessionData = (SessionDto) authentication.getDetails();
		}
		return (sessionData != null ? sessionData
				: new SessionDto(Constants.DEFAULT_USER, new UserId(Constants.DEFAULT_USER), Constants.DEFAULT_TENANT,
						new TenantId(Constants.DEFAULT_TENANT), getCurrentIp(), Constants.DEFAULT_APP));
	}

	/**
	 * Return the current user, or throws an exception, if the user is not
	 * authenticated yet.
	 *
	 * @return the current user
	 */
	public static User getCurrentUser() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		if (authentication != null) {
			if (authentication.getPrincipal() instanceof User) {
				return (User) authentication.getPrincipal();
			}
		}
		throw new IllegalStateException("User not found!");
	}

	/**
	 * If the current user has a specific authority (security role).
	 *
	 * <p>
	 * The name of this method comes from the isUserInRole() method in the
	 * Servlet API
	 * </p>
	 */
	public static boolean isCurrentUserHavePermission(String authority) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		if (authentication != null) {
			if (authentication.getPrincipal() instanceof UserDetails) {
				UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
				return springSecurityUser.getAuthorities().contains(new SimpleGrantedAuthority(authority));
			}
		}
		return false;
	}
}

