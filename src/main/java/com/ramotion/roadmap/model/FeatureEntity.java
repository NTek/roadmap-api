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

    @JsonIgnore
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

    @OneToMany(mappedBy = "feature")
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
        if (application != null ? !application.equals(that.application) : that.application != null) return false;
        if (applicationId != null ? !applicationId.equals(that.applicationId) : that.applicationId != null)
            return false;
        if (auditTimestamps != null ? !auditTimestamps.equals(that.auditTimestamps) : that.auditTimestamps != null)
            return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (localizedFeatures != null ? !localizedFeatures.equals(that.localizedFeatures) : that.localizedFeatures != null)
            return false;
        if (survey != null ? !survey.equals(that.survey) : that.survey != null) return false;
        if (votes != null ? !votes.equals(that.votes) : that.votes != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (applicationId != null ? applicationId.hashCode() : 0);
        result = 31 * result + (implemented ? 1 : 0);
        result = 31 * result + (auditTimestamps != null ? auditTimestamps.hashCode() : 0);
        result = 31 * result + (application != null ? application.hashCode() : 0);
        result = 31 * result + (localizedFeatures != null ? localizedFeatures.hashCode() : 0);
        result = 31 * result + (survey != null ? survey.hashCode() : 0);
        result = 31 * result + (votes != null ? votes.hashCode() : 0);
        return result;
    }
}
