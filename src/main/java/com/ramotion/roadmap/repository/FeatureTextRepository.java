package com.ramotion.roadmap.repository;

import com.ramotion.roadmap.model.FeatureTextEntity;
import com.ramotion.roadmap.model.Language;
import org.springframework.data.repository.CrudRepository;

public interface FeatureTextRepository extends CrudRepository<FeatureTextEntity, Long> {

    FeatureTextEntity findByFeatureIdAndLanguage(long featureId, Language language);

}
