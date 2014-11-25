package com.ramotion.roadmap.repository;

import com.ramotion.roadmap.model.ApplicationEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Oleg Vasiliev on 11.11.2014.
 */

public interface ApplicationRepository extends CrudRepository<ApplicationEntity, Long> {

    ApplicationEntity findByApiToken(String apiToken);
}
