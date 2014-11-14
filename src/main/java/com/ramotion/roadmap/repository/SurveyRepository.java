package com.ramotion.roadmap.repository;

import com.ramotion.roadmap.model.SurveyEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Oleg Vasiliev on 14.11.2014.
 */
public interface SurveyRepository extends CrudRepository<SurveyEntity, Long> {

    List<SurveyEntity> findByApplicationId(long appId);
}
