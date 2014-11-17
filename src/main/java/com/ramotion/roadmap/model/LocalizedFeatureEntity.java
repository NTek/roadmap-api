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
@Table(name = "localized_feature")
@EntityListeners(value = AuditableEntityListener.class)
public class LocalizedFeatureEntity implements EntityWithAuditTimestamps {

    private Long id;
    private String title;

    @Embedded
    private AuditTimestamps auditTimestamps;

    private Long featureId;

    private Integer languageId;

    @JsonIgnore
    private FeatureEntity featureByFeatureId;

    @JsonIgnore
    private LanguageEntity languageByLanguageId;

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "title", nullable = false, insertable = true, updatable = true, length = 45)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public AuditTimestamps getAuditTimestamps() {
        return auditTimestamps;
    }

    @Override
    public void setAuditTimestamps(AuditTimestamps auditTimestamps) {
        this.auditTimestamps = auditTimestamps;
    }

    @Column(name = "feature_id", nullable = false, insertable = false, updatable = false)
    public Long getFeatureId() {
        return featureId;
    }

    public void setFeatureId(Long featureId) {
        this.featureId = featureId;
    }

    @Column(name = "language_id", nullable = false, insertable = false, updatable = false)
    public Integer getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Integer languageId) {
        this.languageId = languageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocalizedFeatureEntity that = (LocalizedFeatureEntity) o;

        if (auditTimestamps != null ? !auditTimestamps.equals(that.auditTimestamps) : that.auditTimestamps != null)
            return false;
        if (featureId != null ? !featureId.equals(that.featureId) : that.featureId != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (languageId != null ? !languageId.equals(that.languageId) : that.languageId != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (auditTimestamps != null ? auditTimestamps.hashCode() : 0);
        result = 31 * result + (featureId != null ? featureId.hashCode() : 0);
        result = 31 * result + (languageId != null ? languageId.hashCode() : 0);
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
    @JoinColumn(name = "language_id", referencedColumnName = "id", nullable = false)
    public LanguageEntity getLanguageByLanguageId() {
        return languageByLanguageId;
    }

    public void setLanguageByLanguageId(LanguageEntity languageByLanguageId) {
        this.languageByLanguageId = languageByLanguageId;
    }
}
