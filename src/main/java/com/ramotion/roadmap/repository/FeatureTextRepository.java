package com.ramotion.roadmap.repository;

import com.ramotion.roadmap.model.FeatureTextEntity;
import com.ramotion.roadmap.model.FeatureTextEntityPK;
import com.ramotion.roadmap.model.Language;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Oleg Vasiliev on 11.11.2014.
 */
public interface FeatureTextRepository extends CrudRepository<FeatureTextEntity, FeatureTextEntityPK> {

    FeatureTextEntity findByFeatureIdAndLanguage(long featureId, Language language);
}
