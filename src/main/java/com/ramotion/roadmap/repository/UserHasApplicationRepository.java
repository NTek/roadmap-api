package com.ramotion.roadmap.repository;

import com.ramotion.roadmap.model.UserHasApplicationEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Oleg Vasiliev on 11.11.2014.
 */

public interface UserHasApplicationRepository extends CrudRepository<UserHasApplicationEntity, Long> {

    List<UserHasApplicationEntity> findByUserId(long userId);

    List<UserHasApplicationEntity> findByApplicationId(long applicationId);

}
