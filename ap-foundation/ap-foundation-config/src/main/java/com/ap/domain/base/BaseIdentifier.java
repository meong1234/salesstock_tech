package com.ap.domain.base;

import java.io.Serializable;

import org.axonframework.domain.IdentifierFactory;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@JsonSerialize(using = ToStringSerializer.class)
public abstract class BaseIdentifier implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String identifier;
	
	public BaseIdentifier() {
        this.identifier = IdentifierFactory.getInstance().generateIdentifier();
    }
}
