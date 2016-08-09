package com.ap.config.security.model;

import com.ap.domain.base.TenantId;
import com.ap.domain.base.UserId;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class UserInformationDto {

	String userName;

	@JsonUnwrapped
	UserId userId;

	String tenantName;

	@JsonUnwrapped
	TenantId tenantId;
}
