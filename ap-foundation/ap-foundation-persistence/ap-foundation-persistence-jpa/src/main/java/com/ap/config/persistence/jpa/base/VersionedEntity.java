package com.ap.config.persistence.jpa.base;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonIgnore;

@MappedSuperclass
@EntityListeners(VersionedEntityListener.class)
public class VersionedEntity extends AbstractEntity implements IVersionedEntity {
	
	/**
	 * 
	 */	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "VERSION")
	@JsonIgnore
    private Integer version;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }    

}
