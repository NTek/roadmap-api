package com.ramotion.roadmap.repository;

import com.ramotion.roadmap.model.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    UserEntity findByEmailAndPassword(String email, String password);

    UserEntity findByEmail(String email);

    UserEntity findByUuid(String uuid);

}
