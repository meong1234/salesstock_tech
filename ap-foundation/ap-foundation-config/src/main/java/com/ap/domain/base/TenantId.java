package com.ap.domain.base;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
@JsonDeserialize(builder = TenantId.TenantIdBuilder.class)
@Value
public class TenantId extends BaseIdentifier {
	
	public TenantId() {
		super();
	}

	@Builder
	public TenantId(String identifier) {
		super(identifier);
	}
	
	@JsonPOJOBuilder(withPrefix = "")
    public static final class TenantIdBuilder {
    }
}
