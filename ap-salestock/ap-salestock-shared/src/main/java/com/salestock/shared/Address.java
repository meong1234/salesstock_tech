package com.salestock.shared;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

@Embeddable
@Value
public class Address {
	
	@Column(name = "addr1")
	private String addr1;
	
	@Column(name = "addr2")
	private String addr2;
	
	@Column(name = "addr3")
	private String addr3;
	
	public Address(@JsonProperty("addr1") String addr1,
			@JsonProperty("addr2") String addr2,
			@JsonProperty("addr3") String addr3) {
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.addr3 = addr3;
    }
}
