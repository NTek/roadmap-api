package com.ramotion.roadmap.model.utils;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ramotion.roadmap.utils.JsonTimestampSerializer;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.sql.Timestamp;

@Embeddable
public class AuditTimestamps {

    @JsonSerialize(using = JsonTimestampSerializer.class)
    private Timestamp createdAt;

    @JsonSerialize(using = JsonTimestampSerializer.class)
    private Timestamp modifiedAt;

    @Column(name = "created_at", nullable = false, insertable = true, updatable = false)
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Column(name = "modified_at", nullable = false, insertable = true, updatable = true)
    public Timestamp getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Timestamp modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuditTimestamps that = (AuditTimestamps) o;

        if (createdAt != null ? !createdAt.equals(that.createdAt) : that.createdAt != null) return false;
        if (modifiedAt != null ? !modifiedAt.equals(that.modifiedAt) : that.modifiedAt != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = createdAt != null ? createdAt.hashCode() : 0;
        result = 31 * result + (modifiedAt != null ? modifiedAt.hashCode() : 0);
        return result;
    }
}
