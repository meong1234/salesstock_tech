package com.ap.domain.base;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
@EqualsAndHashCode
@JsonDeserialize(builder = SessionDto.SessionDtoBuilder.class)
public class SessionDto {
	
	String userName;
	
	UserId userId;
	
	String tenantName;
	
	TenantId tenantId;
	
	String remoteAddress;
	
	String applicationName;
	
	@JsonPOJOBuilder(withPrefix = "")
    public static final class SessionDtoBuilder {
    }
}
