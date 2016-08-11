package com.salestock.shared;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

@Embeddable
@Value
public final class PostalAddress {
	
	@Embedded
	private Address addressLine;
	
	@Column(name = "cityName")
	private String cityName;
	
	@Column(name = "countryCode")
	private String countryCode;
	
	@Column(name = "postalCode")
	private String postalCode;
	
	public PostalAddress(@JsonProperty("addressLine") Address addressLine,
			@JsonProperty("cityName") String cityName,
			@JsonProperty("countryCode") String countryCode,
			@JsonProperty("postalCode") String postalCode) {
        this.addressLine = addressLine;
        this.cityName = cityName;
        this.countryCode = countryCode;
        this.postalCode = postalCode;
    }
	
	public PostalAddress(PostalAddress anAddress) {
        this(anAddress.getAddressLine(), anAddress.getCityName(), anAddress.getCountryCode(), anAddress.getPostalCode());
    }
	
	public PostalAddress withChangedAddressLine(Address addressLine) {
        return new PostalAddress(addressLine, this.getCityName(), this.getCountryCode(), this.getPostalCode());
    }
	
	public PostalAddress withChangedCityName(String cityName) {
        return new PostalAddress(this.getAddressLine(), cityName, this.getCountryCode(), this.getPostalCode());
    }
	
	public PostalAddress withChangedCountryCode(String countryCode) {
        return new PostalAddress(this.getAddressLine(), this.getCityName(), countryCode, this.getPostalCode());
    }
	
	public PostalAddress withChangedPostalCode(String postalCode) {
        return new PostalAddress(this.getAddressLine(), this.getCityName(), this.getCountryCode(), postalCode);
    }
}