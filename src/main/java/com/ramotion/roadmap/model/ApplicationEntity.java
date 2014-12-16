package com.ramotion.roadmap.model;

import com.ramotion.roadmap.model.utils.*;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.UUID;

@Entity
@Table(name = "application")
@EntityListeners(value = {AuditableEntityListener.class, EntityWithUUIDListener.class})
public class ApplicationEntity implements EntityWithAuditTimestamps, EntityWithUUID {

    private Long id;

    private UUID uuid;

    private String apiToken;

    @NotNull(message = "required")
    @NotEmpty(message = "can't be empty")
    private String name;

    private String description;

    private Long activeSurveyId;

    @Embedded
    private AuditTimestamps auditTimestamps;

    private Collection<UserHasApplicationEntity> applicationUsers;

    @Valid
    private Collection<FeatureEntity> applicationFeatures;

    private Collection<SurveyEntity> applicationSurveys;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "uuid", nullable = false, insertable = true, updatable = false)
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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

    @Column(name = "api_token", nullable = false, insertable = true, updatable = true, length = 45)
    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    @Column(name = "active_survey_id", nullable = true, insertable = true, updatable = true)
    public Long getActiveSurveyId() {
        return activeSurveyId;
    }

    public void setActiveSurveyId(Long activeSurvey) {
        this.activeSurveyId = activeSurvey;
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
    public Collection<UserHasApplicationEntity> getApplicationUsers() {
        return applicationUsers;
    }

    public void setApplicationUsers(Collection<UserHasApplicationEntity> userToApplicationsById) {
        this.applicationUsers = userToApplicationsById;
    }

    @OneToMany(mappedBy = "application", cascade = {CascadeType.ALL})
    public Collection<FeatureEntity> getApplicationFeatures() {
        return applicationFeatures;
    }

    public void setApplicationFeatures(Collection<FeatureEntity> applicationFeatures) {
        this.applicationFeatures = applicationFeatures;
    }

    @OneToMany(mappedBy = "application")
    public Collection<SurveyEntity> getApplicationSurveys() {
        return applicationSurveys;
    }

    public void setApplicationSurveys(Collection<SurveyEntity> applicationSurveys) {
        this.applicationSurveys = applicationSurveys;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApplicationEntity that = (ApplicationEntity) o;

        if (activeSurveyId != null ? !activeSurveyId.equals(that.activeSurveyId) : that.activeSurveyId != null)
            return false;
        if (apiToken != null ? !apiToken.equals(that.apiToken) : that.apiToken != null) return false;
        if (auditTimestamps != null ? !auditTimestamps.equals(that.auditTimestamps) : that.auditTimestamps != null)
            return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (uuid != null ? !uuid.equals(that.uuid) : that.uuid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
        result = 31 * result + (apiToken != null ? apiToken.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (activeSurveyId != null ? activeSurveyId.hashCode() : 0);
        result = 31 * result + (auditTimestamps != null ? auditTimestamps.hashCode() : 0);
        return result;
    }
}
