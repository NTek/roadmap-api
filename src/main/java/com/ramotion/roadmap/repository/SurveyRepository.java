package com.ramotion.roadmap.repository;

import com.ramotion.roadmap.model.SurveyEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SurveyRepository extends CrudRepository<SurveyEntity, Long> {

    List<SurveyEntity> findByApplicationId(long appId);

}
