package com.ramotion.roadmap.model;

import java.io.Serializable;

public class VoteEntityPK implements Serializable {

    private String deviceToken;

    private Long surveyId;

    public VoteEntityPK() {
    }

    public VoteEntityPK(String deviceToken, Long surveyId) {
        this.deviceToken = deviceToken;
        this.surveyId = surveyId;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VoteEntityPK that = (VoteEntityPK) o;

        if (deviceToken != null ? !deviceToken.equals(that.deviceToken) : that.deviceToken != null) return false;
        if (surveyId != null ? !surveyId.equals(that.surveyId) : that.surveyId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = deviceToken != null ? deviceToken.hashCode() : 0;
        result = 31 * result + (surveyId != null ? surveyId.hashCode() : 0);
        return result;
    }
}
