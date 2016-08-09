package com.ap.config.persistence.jpa.base;

public interface IVersionedEntity {
    Integer getVersion();
    void setVersion(Integer version);
}

