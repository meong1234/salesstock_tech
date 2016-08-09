package com.ap.config.persistence.jpa.base;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.ap.misc.springutil.ApplicationContextProvider;

public class VersionedEntityListener {
	
	@PrePersist
    public void prePersist(Object entity) {
        if (entity instanceof IVersionedEntity) {
            setCreated((IVersionedEntity) entity);
        }       
    }
    
    @PreUpdate
    public void preUpdate(IVersionedEntity entity) {
    	VersionedEntityChecker check =  this.getChecker();
        check.check(entity);
    }
    
    private VersionedEntityChecker getChecker() {
    	VersionedEntityChecker check = ApplicationContextProvider.getBeanByName("versionedEntityChecker");
        return check;
    }
    
    private void setCreated(IVersionedEntity entity) {
    	Integer submittedVersion = entity.getVersion();
        if (submittedVersion == null) {            
            entity.setVersion(new Integer(0));
        }
    }
    
}
