package com.ramotion.roadmap.dto.web;

import java.util.HashMap;

public class GetSurveyDto {

    private Long surveyId;

    private HashMap<Long, String> features;

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public HashMap<Long, String> getFeatures() {
        return features;
    }

    public void setFeatures(HashMap<Long, String> features) {
        this.features = features;
    }

}
