package com.ramotion.roadmap.model;

import java.io.Serializable;

public class FeatureTextEntityPK implements Serializable {
    private Long featureId;

    private Language language;

    public Long getFeatureId() {
        return featureId;
    }

    public void setFeatureId(Long featureId) {
        this.featureId = featureId;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeatureTextEntityPK that = (FeatureTextEntityPK) o;

        if (featureId != null ? !featureId.equals(that.featureId) : that.featureId != null) return false;
        if (language != that.language) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = featureId != null ? featureId.hashCode() : 0;
        result = 31 * result + (language != null ? language.hashCode() : 0);
        return result;
    }
}
