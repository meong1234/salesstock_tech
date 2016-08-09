package com.ap.config.security.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@JsonDeserialize(builder = LoginCommand.LoginCommandBuilder.class)
@Getter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class LoginCommand {
	
	public static final int PASSWORD_MIN_LENGTH = 4;
    public static final int PASSWORD_MAX_LENGTH = 100;
	
	@Pattern(regexp = "^[a-z0-9]*$")
    @NotNull
    @Size(min = 1, max = 50)
    private String username;
	
	@NotNull
    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;
	
	private Boolean rememberMe;
	
	@JsonPOJOBuilder(withPrefix = "")
    public static final class LoginCommandBuilder {
    }

}
