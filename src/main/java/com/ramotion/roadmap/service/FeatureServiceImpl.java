package com.ramotion.roadmap.service;

import com.ramotion.roadmap.config.AppConfig;
import com.ramotion.roadmap.exceptions.AccessDeniedException;
import com.ramotion.roadmap.model.ApplicationEntity;
import com.ramotion.roadmap.model.FeatureEntity;
import com.ramotion.roadmap.model.UserEntity;
import com.ramotion.roadmap.model.UserHasApplicationEntity;
import com.ramotion.roadmap.repository.FeatureRepository;
import com.ramotion.roadmap.repository.UserHasApplicationRepository;
import com.ramotion.roadmap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FeatureServiceImpl implements FeatureService {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FeatureRepository featureRepository;

    @Autowired
    private UserHasApplicationRepository userHasApplicationRepository;

    @Override
    public void deleteFeatureWithAccessCheck(long featureId, String username) {
        if (username == null) return;
        UserEntity user = userRepository.findByEmail(username);
        if (user == null) return;

        FeatureEntity featureEntity = featureRepository.findOne(featureId);
        if (featureEntity == null) return;

        UserHasApplicationEntity userHasApplicationEntity =
                userHasApplicationRepository.findByUserIdAndApplicationId(user.getId(),
                        featureEntity.getApplicationId());

        if (userHasApplicationEntity == null || userHasApplicationEntity.getAccessLevel() > AppConfig.USER_ACCESS_OWNER)
            throw new AccessDeniedException("You don't nave permissions to do this");

        ApplicationEntity app = featureEntity.getApplication();

        featureRepository.delete(featureEntity);

        //notify subscribers for this app topic, also this action initialize lazy loaded collections
        applicationService.notifyAppUsers(app);
    }

    @Override
    public void deleteFeature(long featureId) {

    }

    @Override
    public boolean deleteLocalizedFeatureWithAccessCheck(long localizedFeatureId) {
        return false;
    }

}
