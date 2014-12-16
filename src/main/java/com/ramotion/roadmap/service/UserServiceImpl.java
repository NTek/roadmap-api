package com.ramotion.roadmap.service;

import com.ramotion.roadmap.dto.EmailPasswordForm;
import com.ramotion.roadmap.exceptions.ValidationException;
import com.ramotion.roadmap.model.UserEntity;
import com.ramotion.roadmap.repository.UserRepository;
import com.ramotion.roadmap.utils.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserRepository getRepository() {
        return userRepository;
    }

    @Override
    public UserEntity registerUser(EmailPasswordForm emailPasswordForm) {
        if (userRepository.findByEmail(emailPasswordForm.getEmail()) != null) {
            throw new ValidationException().withError("email", "email already exist");
        }
        UserEntity user = new UserEntity();
        user.setEmail(emailPasswordForm.getEmail());
        user.setPassword(HashUtil.getSHA1(emailPasswordForm.getPassword(), null));
        user.setEnabled(true);
        userRepository.save(user);
        return user;
    }

    public UserEntity confirmRegistration(String confirmationToken) {
        //TODO: Complete account confirmation logic
        return null;
    }

    @Override
    public UserEntity updateUserProfile(UserEntity entity) {
        if (entity == null || entity.getId() == null) return null;

        UserEntity existedUser = userRepository.findOne(entity.getId());
        if (existedUser == null) return null;

        userRepository.save(entity);

        return null;
    }
}
