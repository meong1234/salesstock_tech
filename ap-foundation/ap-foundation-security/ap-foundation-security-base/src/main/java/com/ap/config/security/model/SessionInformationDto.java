package com.ap.config.security.model;

import com.ap.domain.base.SessionDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class SessionInformationDto {
	
	String tokenInfo;
	
	UserInformationDto userInfo;
	
	public SessionInformationDto(String tokenInfo, SessionDto session) {
		this.tokenInfo = tokenInfo;
		this.userInfo = new UserInformationDto(session.getUserName(), session.getUserId(), session.getTenantName(), session.getTenantId());
	}

}
