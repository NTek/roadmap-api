package com.ramotion.roadmap.model.utils;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.sql.Timestamp;
import java.util.Date;

public class AuditableEntityListener {

    @PrePersist
    void onCreate(Object entity) {
        if (entity instanceof EntityWithAuditTimestamps) {
            EntityWithAuditTimestamps auditableEntity = (EntityWithAuditTimestamps) entity;
            if (auditableEntity.getAuditTimestamps() == null) {
                auditableEntity.setAuditTimestamps(new AuditTimestamps());
            }
            Timestamp now = new Timestamp((new Date()).getTime());
            auditableEntity.getAuditTimestamps().setCreatedAt(now);
            auditableEntity.getAuditTimestamps().setModifiedAt(now);
        }
    }

    @PreUpdate
    void onUpdate(Object entity) {
        if (entity instanceof EntityWithAuditTimestamps) {
            EntityWithAuditTimestamps auditableEntity = (EntityWithAuditTimestamps) entity;
            if (auditableEntity.getAuditTimestamps() == null) {
                auditableEntity.setAuditTimestamps(new AuditTimestamps());
            }
            Timestamp now = new Timestamp((new Date()).getTime());
            auditableEntity.getAuditTimestamps().setModifiedAt(now);
        }
    }
}
