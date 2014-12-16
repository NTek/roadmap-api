package com.ramotion.roadmap.dto;

import java.util.HashSet;

public class GetSurveyDto {

    private Long surveyId;

    private HashSet<Feature> features = new HashSet<>(5); // default value for feature number

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public HashSet<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(HashSet<Feature> features) {
        this.features = features;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GetSurveyDto that = (GetSurveyDto) o;

        if (features != null ? !features.equals(that.features) : that.features != null) return false;
        if (surveyId != null ? !surveyId.equals(that.surveyId) : that.surveyId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = surveyId != null ? surveyId.hashCode() : 0;
        result = 31 * result + (features != null ? features.hashCode() : 0);
        return result;
    }

    public static class Feature {
        private Long featureId;
        private String featureText;

        public Feature() {
        }

        public Feature(Long featureId, String featureText) {
            this.featureId = featureId;
            this.featureText = featureText;
        }

        public Long getFeatureId() {
            return featureId;
        }

        public void setFeatureId(Long featureId) {
            this.featureId = featureId;
        }

        public String getFeatureText() {
            return featureText;
        }

        public void setFeatureText(String featureText) {
            this.featureText = featureText;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Feature that = (Feature) o;

            if (featureId != null ? !featureId.equals(that.featureId) : that.featureId != null) return false;
            if (featureText != null ? !featureText.equals(that.featureText) : that.featureText != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = featureId != null ? featureId.hashCode() : 0;
            result = 31 * result + (featureText != null ? featureText.hashCode() : 0);
            return result;
        }
    }
}
