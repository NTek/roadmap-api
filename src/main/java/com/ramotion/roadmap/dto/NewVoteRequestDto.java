package com.ramotion.roadmap.dto;

/**
 * Created by Oleg Vasiliev on 19.11.2014.
 */
public class NewVoteRequestDto {

    private String surveyId;
    private String deviceToken;
    private String language;

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
