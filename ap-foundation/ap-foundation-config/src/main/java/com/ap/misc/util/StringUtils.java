package com.ap.misc.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringUtils {

	/**
	 * Finds the value of the given enumeration by name, case-insensitive.
	 * Throws an IllegalArgumentException if no match is found.
	 **/
	public static <T extends Enum<T>> T enumValueFromString(Class<T> enumeration, String name) {

		for (T enumValue : enumeration.getEnumConstants()) {
			if (enumValue.name().equalsIgnoreCase(name)) {
				return enumValue;
			}
		}

		throw new IllegalArgumentException(
				String.format("There is no value with name '%s' in Enum %s", name, enumeration.getName()));
	}

	public static String sha256(String rawText) {
		String result = null;
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
			md.update(rawText.getBytes());
			byte byteData[] = md.digest();
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				String hex = Integer.toHexString(0xff & byteData[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}

			result = hexString.toString();
		} catch (NoSuchAlgorithmException e) {
		}
		return result;

	}

}