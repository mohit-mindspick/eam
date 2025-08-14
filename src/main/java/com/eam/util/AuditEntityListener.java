package com.eam.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuditEntityListener {

    private static final Logger auditLogger = LoggerFactory.getLogger("AUDIT_LOGGER");
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @PostPersist
    public void afterPersist(Object entity) {
        logEntity("CREATE", entity);
    }

    @PostUpdate
    public void afterUpdate(Object entity) {
        logEntity("UPDATE", entity);
    }

    @PostRemove
    public void afterRemove(Object entity) {
        logEntity("DELETE", entity);
    }

    private void logEntity(String action, Object entity) {
        try {
            String json = objectMapper.writeValueAsString(entity);
            auditLogger.info("{} - {}", action, json);
        } catch (JsonProcessingException e) {
            auditLogger.error("Error serializing entity for audit log", e);
        }
    }
}