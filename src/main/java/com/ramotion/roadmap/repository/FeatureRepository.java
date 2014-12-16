package com.ramotion.roadmap.repository;

import com.ramotion.roadmap.model.FeatureEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FeatureRepository extends CrudRepository<FeatureEntity, Long> {

    List<FeatureEntity> findByApplicationId(long applicationId);
}
