package com.ramotion.roadmap.service;

import com.ramotion.roadmap.model.ApplicationEntity;
import com.ramotion.roadmap.repository.ApplicationRepository;

import java.util.List;

public interface ApplicationService {

    ApplicationRepository getRepository();

    List<ApplicationEntity> getApplicationsByUser(String userEmail);

    ApplicationEntity createOrUpdateApplication(ApplicationEntity applicationEntity, String ownerEmail);

    void deleteApplication(long appId, String ownerEmail);

}
