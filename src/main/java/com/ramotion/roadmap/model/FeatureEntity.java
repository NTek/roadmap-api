package com.ramotion.roadmap.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ramotion.roadmap.model.utils.AuditTimestamps;
import com.ramotion.roadmap.model.utils.AuditableEntityListener;
import com.ramotion.roadmap.model.utils.EntityWithAuditTimestamps;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Oleg Vasiliev on 12.11.2014.
 */
@Entity
@Table(name = "feature", schema = "", catalog = "roadmap")
@EntityListeners(value = AuditableEntityListener.class)
public class FeatureEntity implements EntityWithAuditTimestamps {

    private Long id;

    private Long applicationId;

    private boolean implemented;

    @Embedded
    private AuditTimestamps auditTimestamps;

    @JsonIgnore
    private ApplicationEntity application;

    @JsonIgnore
    private Collection<LocalizedFeatureEntity> localizedFeaturesById;

    @JsonIgnore
    private Collection<SurveyEntity> surveys;

    @JsonIgnore
    private Collection<VoteEntity> votesById;

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

    @OneToMany(mappedBy = "featureByFeatureId")
    public Collection<LocalizedFeatureEntity> getLocalizedFeaturesById() {
        return localizedFeaturesById;
    }

    public void setLocalizedFeaturesById(Collection<LocalizedFeatureEntity> localizedFeaturesById) {
        this.localizedFeaturesById = localizedFeaturesById;
    }

    @ManyToMany(mappedBy = "surveyFeatures")
    public Collection<SurveyEntity> getSurveys() {
        return surveys;
    }

    public void setSurveys(Collection<SurveyEntity> surveys) {
        this.surveys = surveys;
    }


    @OneToMany(mappedBy = "featureByFeatureId")
    public Collection<VoteEntity> getVotesById() {
        return votesById;
    }

    public void setVotesById(Collection<VoteEntity> votesById) {
        this.votesById = votesById;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeatureEntity that = (FeatureEntity) o;

        if (implemented != that.implemented) return false;
        if (application != null ? !application.equals(that.application) : that.application != null) return false;
        if (applicationId != null ? !applicationId.equals(that.applicationId) : that.applicationId != null)
            return false;
        if (auditTimestamps != null ? !auditTimestamps.equals(that.auditTimestamps) : that.auditTimestamps != null)
            return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (localizedFeaturesById != null ? !localizedFeaturesById.equals(that.localizedFeaturesById) : that.localizedFeaturesById != null)
            return false;
        if (surveys != null ? !surveys.equals(that.surveys) : that.surveys != null) return false;
        if (votesById != null ? !votesById.equals(that.votesById) : that.votesById != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (applicationId != null ? applicationId.hashCode() : 0);
        result = 31 * result + (implemented ? 1 : 0);
        result = 31 * result + (auditTimestamps != null ? auditTimestamps.hashCode() : 0);
        result = 31 * result + (application != null ? application.hashCode() : 0);
        result = 31 * result + (localizedFeaturesById != null ? localizedFeaturesById.hashCode() : 0);
        result = 31 * result + (surveys != null ? surveys.hashCode() : 0);
        result = 31 * result + (votesById != null ? votesById.hashCode() : 0);
        return result;
    }
}
