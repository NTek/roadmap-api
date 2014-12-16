package com.ramotion.roadmap.service;

import com.ramotion.roadmap.dto.EmailPasswordForm;
import com.ramotion.roadmap.model.UserEntity;
import com.ramotion.roadmap.repository.UserRepository;

public interface UserService {

    UserRepository getRepository();

    UserEntity registerUser(EmailPasswordForm emailPasswordForm);

    UserEntity updateUserProfile(UserEntity entity);

}
