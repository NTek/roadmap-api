package com.ramotion.roadmap.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ramotion.roadmap.model.utils.AuditTimestamps;
import com.ramotion.roadmap.model.utils.AuditableEntityListener;
import com.ramotion.roadmap.model.utils.EntityWithAuditTimestamps;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.Collection;

@Entity
@Table(name = "feature")
@EntityListeners(value = AuditableEntityListener.class)
public class FeatureEntity implements EntityWithAuditTimestamps {

    private Long id;

    private Long applicationId;

    private boolean implemented;

    @Embedded
    private AuditTimestamps auditTimestamps;

    @JsonIgnore
    private ApplicationEntity application;

    @Valid
    private Collection<FeatureTextEntity> localizedFeatures;

    @JsonIgnore
    private Collection<SurveyEntity> survey;

    @JsonIgnore
    private Collection<VoteEntity> votes;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "application_id", nullable = false, insertable = false, updatable = false)
    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    @Column(name = "implemented", nullable = false, insertable = true, updatable = true)
    public boolean getImplemented() {
        return implemented;
    }

    public void setImplemented(boolean implemented) {
        this.implemented = implemented;
    }

    public AuditTimestamps getAuditTimestamps() {
        return auditTimestamps;
    }

    public void setAuditTimestamps(AuditTimestamps auditTimestamps) {
        this.auditTimestamps = auditTimestamps;
    }

    @ManyToOne
    @JoinColumn(name = "application_id", referencedColumnName = "id", nullable = false)
    public ApplicationEntity getApplication() {
        return application;
    }

    public void setApplication(ApplicationEntity applicationByApplicationId) {
        this.application = applicationByApplicationId;
        this.applicationId = application != null ? application.getId() : null;
    }

    @OneToMany(mappedBy = "feature", fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    public Collection<FeatureTextEntity> getLocalizedFeatures() {
        return localizedFeatures;
    }

    public void setLocalizedFeatures(Collection<FeatureTextEntity> localizedFeaturesById) {
        this.localizedFeatures = localizedFeaturesById;
    }

    @ManyToMany(mappedBy = "feature")
    public Collection<SurveyEntity> getSurvey() {
        return survey;
    }

    public void setSurvey(Collection<SurveyEntity> surveys) {
        this.survey = surveys;
    }


    @OneToMany(mappedBy = "featureByFeatureId")
    public Collection<VoteEntity> getVotes() {
        return votes;
    }

    public void setVotes(Collection<VoteEntity> votesById) {
        this.votes = votesById;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeatureEntity that = (FeatureEntity) o;

        if (implemented != that.implemented) return false;
        if (applicationId != null ? !applicationId.equals(that.applicationId) : that.applicationId != null)
            return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (applicationId != null ? applicationId.hashCode() : 0);
        result = 31 * result + (implemented ? 1 : 0);
        return result;
    }
}
