package com.ramotion.roadmap.model.utils;

/**
 * Created by Oleg Vasiliev on 13.11.2014.
 */
public interface EntityWithAuditTimestamps {

    AuditTimestamps getAuditTimestamps();

    void setAuditTimestamps(AuditTimestamps o);

}
