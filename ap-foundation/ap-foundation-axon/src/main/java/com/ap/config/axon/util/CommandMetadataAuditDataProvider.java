package com.ap.config.axon.util;

import java.util.Map;

import org.axonframework.auditing.AuditDataProvider;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.domain.MetaData;

import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommandMetadataAuditDataProvider implements AuditDataProvider {
	
	@Override
    public Map<String, Object> provideAuditDataFor(CommandMessage<?> command) {
        final MetaData commandMetaData = command.getMetaData();
        final Map<String, Object> metadata = Maps.newHashMap();
        metadata.putAll(commandMetaData);
        log.debug("commandMessage.metadata = {}", metadata.toString());
        return metadata;
    }

}
