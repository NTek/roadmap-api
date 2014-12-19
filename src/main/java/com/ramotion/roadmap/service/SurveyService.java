package com.ramotion.roadmap.service;

import com.ramotion.roadmap.model.SurveyEntity;

public interface SurveyService {

    SurveyEntity createSurvey(SurveyEntity entity, String username);

    SurveyEntity renameSurvey(long surveyId, String newName, String username);

    SurveyEntity closeSurvey(long surveyId, String username);

    void deleteSurvey();


}
