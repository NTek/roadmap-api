package com.ramotion.roadmap.repository;

import com.ramotion.roadmap.model.AuthTokenEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Oleg Vasiliev on 11.11.2014.
 */
public interface UserAuthTokenRepository extends CrudRepository<AuthTokenEntity, String> {

}
