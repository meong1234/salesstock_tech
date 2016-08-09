package com.ap.config.security.encryption;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ap.misc.util.StringUtils;

@Component
public class SHA256EncryptionService implements PasswordEncoder {

	@Override
	public String encode(CharSequence rawPassword) {
		return StringUtils.sha256(rawPassword.toString());
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return encodedPassword.equalsIgnoreCase(encode(rawPassword));	
	}

}
