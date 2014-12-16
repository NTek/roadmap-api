package com.ramotion.roadmap.repository;

import com.ramotion.roadmap.model.UserHasApplicationEntity;
import com.ramotion.roadmap.model.UserHasApplicationEntityPK;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserHasApplicationRepository extends CrudRepository<UserHasApplicationEntity, UserHasApplicationEntityPK> {

    List<UserHasApplicationEntity> findByUserId(long userId);

    List<UserHasApplicationEntity> getByUserId(long userId);

    List<UserHasApplicationEntity> findDistinctByUserId(long userId);

    List<UserHasApplicationEntity> findByApplicationId(long applicationId);

    UserHasApplicationEntity findByUserIdAndApplicationId(long userId, long applicationId);

}
