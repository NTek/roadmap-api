package com.ramotion.roadmap.service;


public interface FeatureService {

    void deleteFeatureWithAccessCheck(long featureId, String username);

    void deleteFeature(long featureId);


    boolean deleteLocalizedFeatureWithAccessCheck(long localizedFeatureId);

    void deleteLocalizedFeature(long localizedFeatureId);

}
