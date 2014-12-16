package com.ramotion.roadmap.model.utils;

import javax.persistence.PrePersist;
import java.util.UUID;

public class EntityWithUUIDListener {

    @PrePersist
    void onCreate(Object entity) {
        if (entity instanceof EntityWithUUID) {
            EntityWithUUID entityWithUUID = (EntityWithUUID) entity;
            if (entityWithUUID.getUuid() == null) {
                entityWithUUID.setUuid(UUID.randomUUID());
            }
        }
    }
}
