package com.ramotion.roadmap.model;

import com.ramotion.roadmap.model.utils.AuditTimestamps;
import com.ramotion.roadmap.model.utils.AuditableEntityListener;
import com.ramotion.roadmap.model.utils.EntityWithAuditTimestamps;

import javax.persistence.*;

@Entity
@Table(name = "user_has_application")
@EntityListeners(value = AuditableEntityListener.class)
@IdClass(UserHasApplicationEntityPK.class)
public class UserHasApplicationEntity implements EntityWithAuditTimestamps {

    private Long userId;

    private Long applicationId;

    private Byte accessLevel;

    @Embedded
    private AuditTimestamps auditTimestamps;

    private ApplicationEntity applicationByApplicationId;

    private UserEntity userByUserId;

    @Id
    @Column(name = "user_id", nullable = false, insertable = true, updatable = true)
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Id
    @Column(name = "application_id", nullable = false, insertable = true, updatable = true)
    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    @Column(name = "access_level", nullable = false, insertable = true, updatable = true)
    public Byte getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(Byte accessLevel) {
        this.accessLevel = accessLevel;
    }

    @Override
    public AuditTimestamps getAuditTimestamps() {
        return auditTimestamps;
    }

    @Override
    public void setAuditTimestamps(AuditTimestamps auditTimestamps) {
        this.auditTimestamps = auditTimestamps;
    }

    @ManyToOne
    @JoinColumn(name = "application_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public ApplicationEntity getApplicationByApplicationId() {
        return applicationByApplicationId;
    }

    public void setApplicationByApplicationId(ApplicationEntity applicationByApplicationId) {
        this.applicationByApplicationId = applicationByApplicationId;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public UserEntity getUserByUserId() {
        return userByUserId;
    }

    public void setUserByUserId(UserEntity userByUserId) {
        this.userByUserId = userByUserId;
    }
}

