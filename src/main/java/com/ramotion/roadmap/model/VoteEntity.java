package com.ramotion.roadmap.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ramotion.roadmap.model.utils.AuditTimestamps;
import com.ramotion.roadmap.model.utils.AuditableEntityListener;
import com.ramotion.roadmap.model.utils.EntityWithAuditTimestamps;

import javax.persistence.*;

/**
 * Created by Oleg Vasiliev on 12.11.2014.
 */
@Entity
@Table(name = "vote")
@EntityListeners(value = AuditableEntityListener.class)
public class VoteEntity implements EntityWithAuditTimestamps {

    @Id
    private String uuid;

    private String deviceToken;

    private Long surveyId;


    private Long featureId;

    private Language language;

    @Embedded
    private AuditTimestamps auditTimestamps;

    @JsonIgnore
    private FeatureEntity featureByFeatureId;

    @JsonIgnore
    private DeviceEntity deviceByDeviceToken;

    @JsonIgnore
    private SurveyEntity surveyBySurveyId;

    @Id
    @Column(name = "uuid", nullable = false, insertable = true, updatable = true, length = 255)
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Column(name = "device_token", nullable = false, insertable = false, updatable = false, length = 255)
    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    @Column(name = "survey_id", nullable = false, insertable = false, updatable = false)
    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = true, insertable = true, updatable = true)
    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    @Column(name = "feature_id", nullable = false, insertable = false, updatable = false)
    public Long getFeatureId() {
        return featureId;
    }

    public void setFeatureId(Long featureId) {
        this.featureId = featureId;
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

        VoteEntity that = (VoteEntity) o;

        if (auditTimestamps != null ? !auditTimestamps.equals(that.auditTimestamps) : that.auditTimestamps != null)
            return false;
        if (deviceToken != null ? !deviceToken.equals(that.deviceToken) : that.deviceToken != null) return false;
        if (featureId != null ? !featureId.equals(that.featureId) : that.featureId != null) return false;
        if (language != that.language) return false;
        if (surveyId != null ? !surveyId.equals(that.surveyId) : that.surveyId != null) return false;
        if (uuid != null ? !uuid.equals(that.uuid) : that.uuid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = uuid != null ? uuid.hashCode() : 0;
        result = 31 * result + (deviceToken != null ? deviceToken.hashCode() : 0);
        result = 31 * result + (surveyId != null ? surveyId.hashCode() : 0);
        result = 31 * result + (featureId != null ? featureId.hashCode() : 0);
        result = 31 * result + (language != null ? language.hashCode() : 0);
        result = 31 * result + (auditTimestamps != null ? auditTimestamps.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "feature_id", referencedColumnName = "id", nullable = false)
    public FeatureEntity getFeatureByFeatureId() {
        return featureByFeatureId;
    }

    public void setFeatureByFeatureId(FeatureEntity featureByFeatureId) {
        this.featureByFeatureId = featureByFeatureId;
    }

    @ManyToOne
    @JoinColumn(name = "device_token", referencedColumnName = "token", nullable = false)
    public DeviceEntity getDeviceByDeviceToken() {
        return deviceByDeviceToken;
    }

    public void setDeviceByDeviceToken(DeviceEntity deviceByDeviceToken) {
        this.deviceByDeviceToken = deviceByDeviceToken;
    }

    @ManyToOne
    @JoinColumn(name = "survey_id", referencedColumnName = "id", nullable = false)
    public SurveyEntity getSurveyBySurveyId() {
        return surveyBySurveyId;
    }

    public void setSurveyBySurveyId(SurveyEntity surveyBySurveyId) {
        this.surveyBySurveyId = surveyBySurveyId;
    }
}
