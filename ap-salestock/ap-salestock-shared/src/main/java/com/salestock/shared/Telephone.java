package com.salestock.shared;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.axonframework.common.Assert;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Embeddable
@Value
@JsonSerialize(using = ToStringSerializer.class)
@EqualsAndHashCode
@Slf4j
public final class Telephone {
	
	@Column(name = "phoneNumber")
	private String phoneNumber;
	
	public Telephone(Telephone anNumber) {
        this(anNumber.getPhoneNumber());
    }
	
	public Telephone(String phoneNumber) {
		Assert.isTrue(isValid(phoneNumber), "phone number is not valid");
        this.phoneNumber = toInternationalFormat(phoneNumber);
    }
	
	
	public String toString() {
		return this.phoneNumber;
	}
	
	public static boolean isValid(String source) {
		PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
		try {
		  PhoneNumber numberProto = phoneUtil.parse(source, "ID");
		  return phoneUtil.isValidNumber(numberProto);
		} catch (NumberParseException e) {
		  log.error("NumberParseException was thrown: " + e.toString());
		}
		return false;
	}
	
	public static String toInternationalFormat(String source) {
		PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
		try {
		  PhoneNumber numberProto = phoneUtil.parse(source, "ID");
		  return phoneUtil.format(numberProto, PhoneNumberFormat.INTERNATIONAL);
		} catch (NumberParseException e) {
			log.error("NumberParseException was thrown: " + e.toString());
		}
		return "";
	}
}
