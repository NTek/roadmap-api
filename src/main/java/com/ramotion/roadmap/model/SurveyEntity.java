package com.ramotion.roadmap.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ramotion.roadmap.model.utils.AuditTimestamps;
import com.ramotion.roadmap.model.utils.AuditableEntityListener;
import com.ramotion.roadmap.model.utils.EntityWithAuditTimestamps;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
@Table(name = "survey")
@EntityListeners(value = AuditableEntityListener.class)
public class SurveyEntity implements EntityWithAuditTimestamps {

    private Long id;

    private Long applicationId;

    private String title;

    private boolean disabled;

    private Long requiredVotes;

    private Timestamp requiredDate;

    private Timestamp startedAt;

    private Timestamp finishedAt;

    @Embedded
    private AuditTimestamps auditTimestamps;

    @JsonIgnore
    private ApplicationEntity application;

    @JsonIgnore
    private Collection<FeatureEntity> feature;

    @JsonIgnore
    private Collection<VoteEntity> votes;

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "title", nullable = false, insertable = true, updatable = true, length = 255)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "disabled", nullable = false, insertable = true, updatable = true)
    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Column(name = "required_votes", nullable = true, insertable = true, updatable = true)
    public Long getRequiredVotes() {
        return requiredVotes;
    }

    public void setRequiredVotes(Long requiredVotes) {
        this.requiredVotes = requiredVotes;
    }

    @Column(name = "required_date", nullable = true, insertable = true, updatable = true)
    public Timestamp getRequiredDate() {
        return requiredDate;
    }

    public void setRequiredDate(Timestamp requiredDate) {
        this.requiredDate = requiredDate;
    }

    @Column(name = "started_at", nullable = true, insertable = true, updatable = true)
    public Timestamp getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Timestamp startedAt) {
        this.startedAt = startedAt;
    }

    @Column(name = "finished_at", nullable = true, insertable = true, updatable = true)
    public Timestamp getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Timestamp finishedAt) {
        this.finishedAt = finishedAt;
    }

    @Column(name = "application_id", nullable = false, insertable = false, updatable = false)
    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    @Override
    public AuditTimestamps getAuditTimestamps() {
        return auditTimestamps;
    }

    @Override
    public void setAuditTimestamps(AuditTimestamps auditTimestamps) {
        this.auditTimestamps = auditTimestamps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SurveyEntity that = (SurveyEntity) o;

        if (disabled != that.disabled) return false;
        if (applicationId != null ? !applicationId.equals(that.applicationId) : that.applicationId != null)
            return false;
        if (auditTimestamps != null ? !auditTimestamps.equals(that.auditTimestamps) : that.auditTimestamps != null)
            return false;
        if (finishedAt != null ? !finishedAt.equals(that.finishedAt) : that.finishedAt != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (requiredDate != null ? !requiredDate.equals(that.requiredDate) : that.requiredDate != null) return false;
        if (requiredVotes != null ? !requiredVotes.equals(that.requiredVotes) : that.requiredVotes != null)
            return false;
        if (startedAt != null ? !startedAt.equals(that.startedAt) : that.startedAt != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (applicationId != null ? applicationId.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (disabled ? 1 : 0);
        result = 31 * result + (requiredVotes != null ? requiredVotes.hashCode() : 0);
        result = 31 * result + (requiredDate != null ? requiredDate.hashCode() : 0);
        result = 31 * result + (startedAt != null ? startedAt.hashCode() : 0);
        result = 31 * result + (finishedAt != null ? finishedAt.hashCode() : 0);
        result = 31 * result + (auditTimestamps != null ? auditTimestamps.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "application_id", referencedColumnName = "id", nullable = false)
    public ApplicationEntity getApplication() {
        return application;
    }

    public void setApplication(ApplicationEntity applicationByApplicationId) {
        this.application = applicationByApplicationId;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "survey_has_feature")
    public Collection<FeatureEntity> getFeature() {
        return feature;
    }

    public void setFeature(Collection<FeatureEntity> surveyFeatures) {
        this.feature = surveyFeatures;
    }

    @OneToMany(mappedBy = "surveyBySurveyId")
    public Collection<VoteEntity> getVotes() {
        return votes;
    }

    public void setVotes(Collection<VoteEntity> votesById) {
        this.votes = votesById;
    }
}
