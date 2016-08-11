package com.salestock.shared;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.ToString;
import lombok.Value;

@Embeddable
@Value
@ToString
public final class FullName {

	@Column(name = "title")
	private String title;

	@Column(name = "firstName")
	private String firstName;

	@Column(name = "lastName")
	private String lastName;

	public FullName(@JsonProperty("title") String title, @JsonProperty("firstName") String firstName,
			@JsonProperty("lastName") String lastName) {
		this.title = title;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public FullName(FullName aFullName) {
		this(aFullName.getTitle(), aFullName.getFirstName(), aFullName.getLastName());
	}

	public FullName withChangedFirstName(String aFirstName) {
		return new FullName(this.getTitle(), aFirstName, this.getLastName());
	}

	public FullName withChangedLastName(String aLastName) {
		return new FullName(this.getTitle(), this.getFirstName(), aLastName);
	}

	public FullName withChangedtitle(String aTitle) {
		return new FullName(aTitle, this.getFirstName(), this.getLastName());
	}

	public String toStringFullname() {
		return title+" "+firstName+" "+lastName;
	}
}
