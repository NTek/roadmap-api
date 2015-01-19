package com.ramotion.roadmap.service;

import com.ramotion.roadmap.config.AppConfig;
import com.ramotion.roadmap.dto.SurveyDto;
import com.ramotion.roadmap.exceptions.*;
import com.ramotion.roadmap.model.*;
import com.ramotion.roadmap.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class SurveyServiceImpl implements SurveyService {


    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private FeatureRepository featureRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private UserHasApplicationRepository userHasApplicationRepository;

    @Override
    public SurveyEntity createSurvey(SurveyDto dto, String userEmail) {
        if (dto == null || userEmail == null) throw new InternalErrorException("Incorrect method parameters");

        UserEntity authorizedUser = userRepository.findByEmail(userEmail);
        if (authorizedUser == null) throw new UnauthorizedException("User account not found");

        ApplicationEntity existedApplication = applicationRepository.findOne(dto.getApplicationId());
        if (existedApplication == null) throw new NotFoundException("Application not found");

        UserHasApplicationEntity userHasApplicationEntity =
                userHasApplicationRepository.findOne(new UserHasApplicationEntityPK(authorizedUser.getId(), existedApplication.getId()));
        if (userHasApplicationEntity.getAccessLevel() > AppConfig.USER_ACCESS_EDIT)
            throw new AccessDeniedException("You can't create surveys for this app");

        if (existedApplication.getActiveSurveyId() != null) {
            throw new ValidationException().withError("applicationId", "this application already has a active survey");
        }

        Set<FeatureEntity> features = new HashSet<>(dto.getFeatures().size());

        for (Long featureId : dto.getFeatures()) {
            FeatureEntity featureEntity = featureRepository.findOne(featureId);
            if (!featureEntity.getApplicationId().equals(dto.getApplicationId())) {
                throw new ValidationException().withError("features", "You can't use features from other app");
            }
            if (featureEntity.getImplemented()) {
                throw new ValidationException().withError("features", "You can't use implemented features");

            }
            features.add(featureEntity);
        }

        SurveyEntity newSurvey = new SurveyEntity();
        newSurvey.setApplicationId(dto.getApplicationId());
        newSurvey.setTitle(dto.getTitle());
        newSurvey.setUuid(UUID.randomUUID());
        newSurvey.setRequiredVotes(dto.getRequiredVotes());
        newSurvey.setRequiredDate(dto.getRequiredDate());
        newSurvey.setFeature(features);
        newSurvey.setStartedAt(new Timestamp(System.currentTimeMillis()));

        surveyRepository.save(newSurvey);

        existedApplication.setActiveSurveyId(newSurvey.getId());
        applicationRepository.save(existedApplication);

        applicationService.notifyAppUsers(existedApplication);
        return newSurvey;
    }

    @Override
    public void deleteSurvey(long surveyId, String userEmail) {
        if (userEmail == null) throw new InternalErrorException("Incorrect method parameters");
        UserEntity authorizedUser = userRepository.findByEmail(userEmail);
        if (authorizedUser == null) throw new UnauthorizedException("User account not found");
        SurveyEntity survey = surveyRepository.findOne(surveyId);
        if (survey == null) throw new NotFoundException("Survey not found");

        UserHasApplicationEntity userHasApplicationEntity =
                userHasApplicationRepository.findOne(new UserHasApplicationEntityPK(authorizedUser.getId(), survey.getApplicationId()));
        if (userHasApplicationEntity.getAccessLevel() > AppConfig.USER_ACCESS_EDIT)
            throw new AccessDeniedException("You can't delete surveys for this app");

        ApplicationEntity app = survey.getApplication();
        if (app.getActiveSurveyId().equals(surveyId)) {
            app.setActiveSurveyId(null);
            applicationRepository.save(app);
        }

        surveyRepository.delete(survey);

        applicationService.notifyAppUsers(app);

    }

    @Override
    public SurveyEntity enableSurvey(long surveyId, String userEmail) {
        return setSurveyDisabledFlag(surveyId, userEmail, false);
    }

    @Override
    public SurveyEntity disableSurvey(long surveyId, String userEmail) {
        return setSurveyDisabledFlag(surveyId, userEmail, true);
    }

    @Override
    public SurveyEntity closeSurvey(long surveyId, String userEmail) {
        if (userEmail == null) throw new InternalErrorException("Incorrect method parameters");

        UserEntity authorizedUser = userRepository.findByEmail(userEmail);
        if (authorizedUser == null) throw new UnauthorizedException("User account not found");
        SurveyEntity survey = surveyRepository.findOne(surveyId);
        if (survey == null) throw new NotFoundException("Survey not found");

        UserHasApplicationEntity userHasApplicationEntity =
                userHasApplicationRepository.findOne(new UserHasApplicationEntityPK(authorizedUser.getId(), survey.getApplicationId()));
        if (userHasApplicationEntity.getAccessLevel() > AppConfig.USER_ACCESS_EDIT)
            throw new AccessDeniedException("You can't close surveys in this app");

        ApplicationEntity app = survey.getApplication();
        app.setActiveSurveyId(null);
        applicationRepository.save(app);
        survey.setFinishedAt(new Timestamp(System.currentTimeMillis()));
        surveyRepository.save(survey);
        applicationService.notifyAppUsers(app);
        return survey;
    }

    @Override
    public SurveyEntity renameSurvey(long surveyId, String name, String userEmail) {
        if (userEmail == null) throw new InternalErrorException("Incorrect method parameters");

        UserEntity authorizedUser = userRepository.findByEmail(userEmail);
        if (authorizedUser == null) throw new UnauthorizedException("User account not found");
        SurveyEntity survey = surveyRepository.findOne(surveyId);
        if (survey == null) throw new NotFoundException("Survey not found");

        UserHasApplicationEntity userHasApplicationEntity =
                userHasApplicationRepository.findOne(new UserHasApplicationEntityPK(authorizedUser.getId(), survey.getApplicationId()));
        if (userHasApplicationEntity.getAccessLevel() > AppConfig.USER_ACCESS_EDIT)
            throw new AccessDeniedException("You can't rename this survey");

        survey.setTitle(name);
        surveyRepository.save(survey);
        applicationService.notifyAppUsers(survey.getApplication());
        return survey;
    }


    private SurveyEntity setSurveyDisabledFlag(long surveyId, String userEmail, boolean isDisabled) {
        if (userEmail == null) throw new InternalErrorException("Incorrect method parameters");
        UserEntity authorizedUser = userRepository.findByEmail(userEmail);
        if (authorizedUser == null) throw new UnauthorizedException("User account not found");
        SurveyEntity survey = surveyRepository.findOne(surveyId);
        if (survey == null) throw new NotFoundException("Survey not found");

        if (survey.getFinishedAt() != null)
            throw new NotFoundException("Active survey with that id not found");

        UserHasApplicationEntity userHasApplicationEntity =
                userHasApplicationRepository.findOne(new UserHasApplicationEntityPK(authorizedUser.getId(), survey.getApplicationId()));
        if (userHasApplicationEntity.getAccessLevel() > AppConfig.USER_ACCESS_EDIT)
            throw new AccessDeniedException("You can't delete surveys for this app");

        survey.setDisabled(isDisabled);
        surveyRepository.save(survey);
        applicationService.notifyAppUsers(survey.getApplication());
        return survey;
    }

}
