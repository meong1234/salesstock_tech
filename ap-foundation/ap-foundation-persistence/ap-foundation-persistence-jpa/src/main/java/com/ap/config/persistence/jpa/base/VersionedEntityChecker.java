package com.ap.config.persistence.jpa.base;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import javax.persistence.Column;
import javax.persistence.OptimisticLockException;
import javax.persistence.Table;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

@Component
public class VersionedEntityChecker {
	
	private JdbcTemplate jdbcTemplate;
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);    
    }        
    
    public void check(IVersionedEntity entity) {
        log.debug("start check AbstractVersionedEntity");
        
        Integer submittedVersion = entity.getVersion();
        if (submittedVersion == null) {
            throw new RuntimeException("Submitted entity must have a version");
        }
        
        Class<?> entityClass = entity.getClass();
        log.debug("entityClass : "+entityClass);
        
        Annotation tableAnn = AnnotationUtils.findAnnotation(entityClass, Table.class);
        String tableName = (String) AnnotationUtils.getValue(tableAnn, "name");
        log.debug("tableName : "+tableName);
        
        Field idField = ReflectionUtils.findField(entityClass, "id");
        ReflectionUtils.makeAccessible(idField);
        
        //get value
        //String idvalue = ReflectionUtils.getField(idField, entity).toString();
        //log.debug("idvalue {}", idvalue);
        
        //get column name  
        String idColName;
        Annotation idColAnn = idField.getAnnotation(Column.class);
        if (idColAnn != null) {
            idColName = (String) AnnotationUtils.getValue(idColAnn, "name");    
        } else {
            idColName = "id";
        }             
                           
           
        String sql = "select version from " + tableName
            + " where " + idColName + "= ?" ;
        
        log.debug("sql version : "+sql);
        Integer latestVersion = jdbcTemplate.queryForObject(sql, Integer.class, ReflectionUtils.getField(idField, entity));
        
        
        if (submittedVersion != latestVersion) {
            throw new OptimisticLockException("Stale entity: submitted version " + submittedVersion
                    + ", but latest version is " + latestVersion);
        }
        
        entity.setVersion(entity.getVersion() + 1);
        log.debug("end check AbstractVersionedEntity");
    }

}
