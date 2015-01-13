package com.ramotion.roadmap.service;

import com.ramotion.roadmap.config.AppConfig;
import com.ramotion.roadmap.dto.SurveyDto;
import com.ramotion.roadmap.exceptions.*;
import com.ramotion.roadmap.model.*;
import com.ramotion.roadmap.repository.*;
import com.ramotion.roadmap.utils.APIMappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class SurveyServiceImpl implements SurveyService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

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
        if (dto == null) throw new InternalErrorException("Incorrect method parameters");

        UserEntity authorizedUser = userRepository.findByEmail(userEmail);
        if (authorizedUser == null) throw new UnauthorizedException("User account not found");

        UserHasApplicationEntity userHasApplicationEntity = userHasApplicationRepository.findOne(new UserHasApplicationEntityPK());
        if (userHasApplicationEntity.getAccessLevel() > AppConfig.USER_ACCESS_EDIT)
            throw new AccessDeniedException("You can't create surveys for this app");

        ApplicationEntity existedApplication = applicationRepository.findOne(dto.getApplicationId());
        if (existedApplication == null) throw new NotFoundException("Application not found");

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

        surveyRepository.save(newSurvey);

        ApplicationEntity savedApp = applicationRepository.findOne(newSurvey.getApplicationId());

        //notify app users, also this action initialize lazy loaded collections
        for (UserHasApplicationEntity userAccess : savedApp.getApplicationUsers()) {
            messagingTemplate.convertAndSendToUser(userAccess.getUserByUserId().getEmail(), APIMappings.Socket.TOPIC_APPS, savedApp);
        }

        return newSurvey;
    }

    @Override
    public void deleteSurvey(long surveyId, String username) {

    }


}
