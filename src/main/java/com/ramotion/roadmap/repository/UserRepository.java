package com.ramotion.roadmap.repository;

import com.ramotion.roadmap.model.UserEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Oleg Vasiliev on 11.11.2014.
 */
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    UserEntity findByEmailAndPassword(String email, String password);

}
