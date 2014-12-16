package com.ramotion.roadmap.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ramotion.roadmap.model.utils.AuditTimestamps;
import com.ramotion.roadmap.model.utils.AuditableEntityListener;
import com.ramotion.roadmap.model.utils.EntityWithAuditTimestamps;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "feature_text")
@EntityListeners(value = AuditableEntityListener.class)
public class FeatureTextEntity implements EntityWithAuditTimestamps {

    private Long id;

    private Long featureId;

    @NotNull(message = "required")
    private Language language;

    @NotNull(message = "required")
    @NotEmpty(message = "can't be empty")
    private String text;

    @Embedded
    private AuditTimestamps auditTimestamps;

    @JsonIgnore
    private FeatureEntity feature;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "feature_id", nullable = false, insertable = false, updatable = false)
    public Long getFeatureId() {
        return featureId;
    }

    public void setFeatureId(Long featureId) {
        this.featureId = featureId;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false, insertable = true, updatable = true)
    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    @Column(name = "text", nullable = false, insertable = true, updatable = true)
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
    @JoinColumn(name = "feature_id", referencedColumnName = "id", nullable = false)
    public FeatureEntity getFeature() {
        return feature;
    }

    public void setFeature(FeatureEntity featureByFeatureId) {
        this.feature = featureByFeatureId;
        if (featureByFeatureId != null) this.featureId = featureByFeatureId.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeatureTextEntity that = (FeatureTextEntity) o;

        if (auditTimestamps != null ? !auditTimestamps.equals(that.auditTimestamps) : that.auditTimestamps != null)
            return false;
        if (feature != null ? !feature.equals(that.feature) : that.feature != null)
            return false;
        if (featureId != null ? !featureId.equals(that.featureId) : that.featureId != null) return false;
        if (language != that.language) return false;
        if (text != null ? !text.equals(that.text) : that.text != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = featureId != null ? featureId.hashCode() : 0;
        result = 31 * result + (language != null ? language.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (auditTimestamps != null ? auditTimestamps.hashCode() : 0);
        result = 31 * result + (feature != null ? feature.hashCode() : 0);
        return result;
    }
}


