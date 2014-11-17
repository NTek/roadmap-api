package com.ramotion.roadmap.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ramotion.roadmap.model.utils.AuditTimestamps;
import com.ramotion.roadmap.model.utils.AuditableEntityListener;
import com.ramotion.roadmap.model.utils.EntityWithAuditTimestamps;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Oleg Vasiliev on 11.11.2014.
 */
@Entity
@Table(name = "application")
@EntityListeners(value = AuditableEntityListener.class)
public class ApplicationEntity implements EntityWithAuditTimestamps {

    private Long id;
    private String name;
    private String description;
    private String apiKey;
    private String apiToken;

    @Embedded
    private AuditTimestamps auditTimestamps;

    @JsonIgnore
    private Collection<UserHasApplicationEntity> userToApplicationsById;

    @JsonIgnore
    private Collection<FeatureEntity> applicationFeatures;

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "name", nullable = false, insertable = true, updatable = true, length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description", nullable = true, insertable = true, updatable = true, length = 255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "api_key", nullable = false, insertable = true, updatable = true, length = 45)
    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Column(name = "api_token", nullable = false, insertable = true, updatable = true, length = 45)
    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    @Override
    public AuditTimestamps getAuditTimestamps() {
        return auditTimestamps;
    }

    @Override
    public void setAuditTimestamps(AuditTimestamps auditTimestamps) {
        this.auditTimestamps = auditTimestamps;
    }


    @OneToMany(mappedBy = "applicationByApplicationId")
    public Collection<UserHasApplicationEntity> getUserToApplicationsById() {
        return userToApplicationsById;
    }

    public void setUserToApplicationsById(Collection<UserHasApplicationEntity> userToApplicationsById) {
        this.userToApplicationsById = userToApplicationsById;
    }

    @OneToMany(mappedBy = "application")
    public Collection<FeatureEntity> getApplicationFeatures() {
        return applicationFeatures;
    }

    public void setApplicationFeatures(Collection<FeatureEntity> applicationFeatures) {
        this.applicationFeatures = applicationFeatures;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApplicationEntity that = (ApplicationEntity) o;

        if (apiKey != null ? !apiKey.equals(that.apiKey) : that.apiKey != null) return false;
        if (apiToken != null ? !apiToken.equals(that.apiToken) : that.apiToken != null) return false;
        if (applicationFeatures != null ? !applicationFeatures.equals(that.applicationFeatures) : that.applicationFeatures != null)
            return false;
        if (auditTimestamps != null ? !auditTimestamps.equals(that.auditTimestamps) : that.auditTimestamps != null)
            return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (userToApplicationsById != null ? !userToApplicationsById.equals(that.userToApplicationsById) : that.userToApplicationsById != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (apiKey != null ? apiKey.hashCode() : 0);
        result = 31 * result + (apiToken != null ? apiToken.hashCode() : 0);
        result = 31 * result + (auditTimestamps != null ? auditTimestamps.hashCode() : 0);
        result = 31 * result + (userToApplicationsById != null ? userToApplicationsById.hashCode() : 0);
        result = 31 * result + (applicationFeatures != null ? applicationFeatures.hashCode() : 0);
        return result;
    }
}
