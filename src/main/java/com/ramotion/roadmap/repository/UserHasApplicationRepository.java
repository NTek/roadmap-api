package com.ramotion.roadmap.repository;

import com.ramotion.roadmap.model.UserHasApplicationEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Oleg Vasiliev on 11.11.2014.
 */

public interface UserHasApplicationRepository extends CrudRepository<UserHasApplicationEntity, Long> {

}
