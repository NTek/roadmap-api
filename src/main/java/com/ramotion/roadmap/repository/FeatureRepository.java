package com.ramotion.roadmap.repository;

import com.ramotion.roadmap.model.FeatureEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Oleg Vasiliev on 11.11.2014.
 */
public interface FeatureRepository extends CrudRepository<FeatureEntity, Long> {

    List<FeatureEntity> findByApplicationId(long applicationId);
}
