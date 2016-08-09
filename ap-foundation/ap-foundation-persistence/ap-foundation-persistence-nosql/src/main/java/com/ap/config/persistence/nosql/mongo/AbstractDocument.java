package com.ap.config.persistence.nosql.mongo;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public abstract class AbstractDocument {

	/** ID */
    @Id
    private String id;

    /** Created date */
    @JsonIgnore
    private LocalDateTime createdDate;

    /** Last update */
    @JsonIgnore
    private LocalDateTime lastUpdate;
    
    private Boolean active;
    
    public AbstractDocument(String id) {
    	this.id = id;
    	this.active = true;
    }
    
    public AbstractDocument(String id, Boolean active) {
    	this.id = id;
    	this.active = true;
    	if (active != null) {
    		this.active = active;
    	}
    }
    
    public void deActivated() {
    	if (this.active) {
    		this.active = false;
    	}
    }

    @JsonIgnore
    public String getIdAsString() {
        String result = null;

        if (id != null) {
            result = id.toString();
        }

        return result;
    }

}
