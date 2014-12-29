package com.ramotion.roadmap.service;

import com.ramotion.roadmap.config.AppConfig;
import com.ramotion.roadmap.exceptions.AccessDeniedException;
import com.ramotion.roadmap.exceptions.InternalErrorException;
import com.ramotion.roadmap.exceptions.NotFoundException;
import com.ramotion.roadmap.exceptions.UnauthorizedException;
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
        if (user == null) throw new UnauthorizedException("User account not found");
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
        if (entity == null || ownerEmail == null) throw new InternalErrorException("Incorrect method parameters");

        UserEntity existedUser = userRepository.findByEmail(ownerEmail);
        if (existedUser == null) throw new UnauthorizedException("User account not found");

        entity.setId(null);
        entity.setApplicationUsers(null);
        entity.setApiToken(generateAPIToken());

        for (FeatureEntity featureEntity : entity.getApplicationFeatures()) {
            featureEntity.setId(null);
            featureEntity.setApplication(entity);
            for (FeatureTextEntity localized : featureEntity.getLocalizedFeatures()) {
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
        if (entity == null || userEmail == null) throw new InternalErrorException("Incorrect method parameters");

        UserEntity existedUser = userRepository.findByEmail(userEmail);
        if (existedUser == null) throw new UnauthorizedException("User account not found");

        ApplicationEntity existedApp = applicationRepository.findOne(appId);

        if (existedApp == null) throw new NotFoundException("Application not found");

        UserHasApplicationEntity userAccessLevel = userHasApplicationRepository.findByUserIdAndApplicationId(existedUser.getId(), appId);

        if (userAccessLevel == null || userAccessLevel.getAccessLevel() > AppConfig.USER_ACCESS_EDIT)
            throw new AccessDeniedException();

        existedApp.setName(entity.getName());
        if (entity.getApiToken() != null) existedApp.setApiToken(entity.getApiToken());
        existedApp.setApplicationFeatures(entity.getApplicationFeatures());
        existedApp.setDescription(entity.getDescription());

//        entity.setId(appId);

        for (FeatureEntity featureEntity : existedApp.getApplicationFeatures()) {

            if (featureEntity.getId() != null
                    && featureEntity.getApplicationId() != null
                    && featureEntity.getApplicationId() != appId) {
                featureEntity.setId(null);
            }
            featureEntity.setApplication(existedApp);

            for (FeatureTextEntity localized : featureEntity.getLocalizedFeatures()) {
                localized.setFeature(featureEntity);
            }
        }

        applicationRepository.save(existedApp);

        ApplicationEntity savedApp = applicationRepository.findOne(existedApp.getId());

        //notify app users, also this action initialize lazy loaded collections
        for (UserHasApplicationEntity userAccess : savedApp.getApplicationUsers()) {
            messagingTemplate.convertAndSendToUser(userAccess.getUserByUserId().getEmail(), APIMappings.Socket.TOPIC_APPS, savedApp);
        }

        return savedApp;
    }

    @Override
    public void deleteApplication(long appId, String ownerEmail) {

    }

    private String generateAPIToken() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
