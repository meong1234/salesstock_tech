package com.ap.config.axon.util.correlationevent;

import java.util.Objects;
import java.util.UUID;


/**
 * A {@code String} based identifying token used to correlate commands and events to each other.
 */
public final class CorrelationToken {

    public static final String KEY = "correlationToken";

    private final String token;

    /**
     * Create a {@code CorrelationToken} with a {@code UUID} based token.
     */
    public CorrelationToken() {
        this.token = UUID.randomUUID().toString();
    }

    /**
     * Gets the token.
     *
     * @return the token.
     */
    public String getToken() {
        return token;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(token);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final CorrelationToken other = (CorrelationToken) obj;
        return Objects.equals(this.token, other.token);
    }

    @Override
	public String toString() {
		return "CorrelationToken [token=" + token + "]";
	}
    
    
}
