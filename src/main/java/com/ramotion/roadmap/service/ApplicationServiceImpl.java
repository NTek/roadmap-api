package com.ramotion.roadmap.service;

import com.ramotion.roadmap.config.AppConfig;
import com.ramotion.roadmap.exceptions.AccessDeniedException;
import com.ramotion.roadmap.exceptions.ValidationException;
import com.ramotion.roadmap.model.*;
import com.ramotion.roadmap.repository.ApplicationRepository;
import com.ramotion.roadmap.repository.UserHasApplicationRepository;
import com.ramotion.roadmap.repository.UserRepository;
import com.ramotion.roadmap.utils.APIMappings;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserHasApplicationRepository userHasApplicationRepository;

    @Override
    public ApplicationRepository getRepository() {
        return applicationRepository;
    }


    @Override
    public List<ApplicationEntity> getApplicationsByUser(String userEmail) {
        UserEntity user = userRepository.findByEmail(userEmail);
        if (user == null) return null;
        List<UserHasApplicationEntity> userApps = userHasApplicationRepository.findByUserId(user.getId());
        List<ApplicationEntity> apps = new ArrayList<>(userApps.size());
        for (UserHasApplicationEntity userApp : userApps) {
            //Initialize hibernate proxies
            ApplicationEntity app = userApp.getApplicationByApplicationId();
            Hibernate.initialize(app.getApplicationUsers());
            Hibernate.initialize(app.getApplicationFeatures());
            for (SurveyEntity survey : app.getApplicationSurveys()) {
                Hibernate.initialize(survey.getFeature());
            }
            apps.add(app);
        }
        return apps;
    }

    @Override
    public ApplicationEntity createApplication(ApplicationEntity entity, String ownerEmail) {
        if (entity == null || ownerEmail == null) return null;

        UserEntity existedUser = userRepository.findByEmail(ownerEmail);
        if (existedUser == null) return null;

        entity.setId(null);
        entity.setApplicationUsers(null);
        entity.setApiToken(generateAPIToken());

        for (FeatureEntity featureEntity : entity.getApplicationFeatures()) {
            featureEntity.setId(null);
            featureEntity.setApplication(entity);
            for (FeatureTextEntity localized : featureEntity.getLocalizedFeatures()) {
                localized.setId(null);
                localized.setFeature(featureEntity);
            }
        }

        applicationRepository.save(entity);

        //create owner record for app
        UserHasApplicationEntity owner = new UserHasApplicationEntity(existedUser.getId(), entity.getId(), AppConfig.USER_ACCESS_OWNER);
        userHasApplicationRepository.save(owner);

        ApplicationEntity savedApp = applicationRepository.findOne(entity.getId());
        //notify app user, also this action initialize lazy loaded collections
        messagingTemplate.convertAndSendToUser(ownerEmail, APIMappings.Socket.TOPIC_APPS, savedApp);
        return savedApp;
    }

    @Override
    public ApplicationEntity editApplication(long appId, ApplicationEntity entity, String userEmail) {
        if (entity == null || userEmail == null) return null;

        UserEntity existedUser = userRepository.findByEmail(userEmail);

        ApplicationEntity existedApp = applicationRepository.findOne(appId);

        if (existedApp == null || existedUser == null) return null;

        UserHasApplicationEntity userAccessLevel = userHasApplicationRepository.findByUserIdAndApplicationId(existedUser.getId(), existedApp.getId());

        if (userAccessLevel == null || userAccessLevel.getAccessLevel() > AppConfig.USER_ACCESS_EDIT)
            throw new AccessDeniedException();

        existedApp.setName(entity.getName());
        if (entity.getApiToken() != null) existedApp.setApiToken(entity.getApiToken());
        existedApp.setApplicationFeatures(entity.getApplicationFeatures());
        existedApp.setDescription(entity.getDescription());

        for (FeatureEntity featureEntity : existedApp.getApplicationFeatures()) {
            if (featureEntity.getApplicationId() != null && featureEntity.getApplicationId() != appId)
                throw new ValidationException().withError("applicationFeatures", "you can't attach existed feature to another app");
            featureEntity.setApplication(existedApp);
            for (FeatureTextEntity localized : featureEntity.getLocalizedFeatures()) {
                if (localized.getFeatureId() != null && !localized.getFeatureId().equals(featureEntity.getId()))
                    throw new ValidationException().withError("applicationFeatures", "you can't attach existed localization text to another feature");
                localized.setFeature(featureEntity);
            }
        }

        applicationRepository.save(existedApp);

        ApplicationEntity savedApp = applicationRepository.findOne(entity.getId());

        //notify app users, also this action initialize lazy loaded collections
        for (UserHasApplicationEntity userAccess : savedApp.getApplicationUsers()) {
            messagingTemplate.convertAndSendToUser(userAccess.getUserByUserId().getEmail(), APIMappings.Socket.TOPIC_APPS, savedApp);
        }

        return savedApp;
    }

    @Override
    public ApplicationEntity createOrUpdateApplication(ApplicationEntity entity, String ownerEmail) {
        if (entity == null || ownerEmail == null) return null;

        UserEntity existedUser = userRepository.findByEmail(ownerEmail);
        if (existedUser == null) return null;

        ApplicationEntity existedApp = null;
        if (entity.getId() != null)
            existedApp = applicationRepository.findOne(entity.getId());

        if (existedApp == null) {
            //it's a creating
            existedApp = entity;
            existedApp.setApplicationUsers(null);
            existedApp.setApiToken(generateAPIToken());

            for (FeatureEntity featureEntity : existedApp.getApplicationFeatures()) {
                featureEntity.setApplication(existedApp);
                for (FeatureTextEntity localized : featureEntity.getLocalizedFeatures()) {
                    localized.setFeature(featureEntity);
                }
            }
            applicationRepository.save(existedApp);

            //create owner record for app
            UserHasApplicationEntity owner = new UserHasApplicationEntity(existedUser.getId(), existedApp.getId(), AppConfig.USER_ACCESS_OWNER);
            userHasApplicationRepository.save(owner);

        } else {
            //Check user access level - if user can't edit app - throw error!
            UserHasApplicationEntity accessObject = userHasApplicationRepository.findByUserIdAndApplicationId(existedUser.getId(), existedApp.getId());
            if (accessObject == null || accessObject.getAccessLevel() > AppConfig.USER_ACCESS_OWNER)
                throw new AccessDeniedException();

            //it's updating
            existedApp.setName(entity.getName());
            if (entity.getApiToken() != null) existedApp.setApiToken(entity.getApiToken());
            existedApp.setApplicationFeatures(entity.getApplicationFeatures());
            existedApp.setDescription(entity.getDescription());

            for (FeatureEntity featureEntity : existedApp.getApplicationFeatures()) {
                featureEntity.setApplication(existedApp);
                for (FeatureTextEntity localized : featureEntity.getLocalizedFeatures()) {
                    localized.setFeature(featureEntity);
                }
            }
            applicationRepository.save(existedApp);
        }

        existedApp = applicationRepository.findOne(existedApp.getId());

        //notify subscribers for this app topic, also this action initialize lazy loaded collections
        messagingTemplate.convertAndSend(APIMappings.Socket.TOPIC_APPS + "/" + existedApp.getUuid(), existedApp);

        return existedApp;
    }

    @Override
    public void deleteApplication(long appId, String ownerEmail) {

    }

    private String generateAPIToken() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
