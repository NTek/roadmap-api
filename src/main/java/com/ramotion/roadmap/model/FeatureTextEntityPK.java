package com.ramotion.roadmap.model;

import java.io.Serializable;

public class FeatureTextEntityPK implements Serializable {

//    private Long featureId;

    private Language language;

    private FeatureEntity feature;

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

//    public Long getFeatureId() {
//        return featureId;
//    }
//
//    public void setFeatureId(Long feature_id) {
//        this.featureId = feature_id;
//    }


    public FeatureEntity getFeature() {
        return feature;
    }

    public void setFeature(FeatureEntity feature) {
        this.feature = feature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeatureTextEntityPK that = (FeatureTextEntityPK) o;

        if (feature != null ? !feature.equals(that.feature) : that.feature != null) return false;
        if (language != that.language) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = language != null ? language.hashCode() : 0;
        result = 31 * result + (feature != null ? feature.hashCode() : 0);
        return result;
    }
}
