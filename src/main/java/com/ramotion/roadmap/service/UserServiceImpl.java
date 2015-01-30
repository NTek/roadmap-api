package com.ramotion.roadmap.service;

import com.ramotion.roadmap.dto.EmailPasswordForm;
import com.ramotion.roadmap.dto.UserProfile;
import com.ramotion.roadmap.exceptions.NotFoundException;
import com.ramotion.roadmap.exceptions.ValidationException;
import com.ramotion.roadmap.model.UserEntity;
import com.ramotion.roadmap.repository.UserRepository;
import com.ramotion.roadmap.utils.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailerService mailerService;

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

    @Override
    public UserProfile confirmNewEmail(String confirmationToken) {
        if (confirmationToken == null) return null;
        UserEntity userEntity = userRepository.findByEmailConfirmationToken(confirmationToken);
        if (userEntity == null) {
            throw new NotFoundException("Confirmation token not found");
        }
        String newEmail = userEntity.getNewEmail();
        UserEntity userWithTargetEmail = userRepository.findByEmail(newEmail);
        if (userWithTargetEmail != null) {
            userEntity.setNewEmail(null);
            userEntity.setEmailConfirmationToken(null);
            userRepository.save(userEntity);
            throw new ValidationException("Email already exist").withError("email", "This email already registered.");
        }
        userEntity.setEmail(newEmail);
        userEntity.setEmailConfirmationToken(null);
        userEntity.setNewEmail(null);
        userRepository.save(userEntity);
        return new UserProfile(userEntity.getId(), userEntity.getEmail());
    }

    @Override
    public UserProfile getUserProfile(String userEmail) {
        if (userEmail == null) return null;
        UserEntity user = userRepository.findByEmail(userEmail);
        return new UserProfile(user.getId(), user.getEmail());
    }

    @Override
    public void changeEmail(EmailPasswordForm emailPasswordForm, String userEmail) {
        if (userEmail == null) return;
        UserEntity user = userRepository.findByEmail(userEmail);
        if (!user.getPassword().equals(HashUtil.getSHA1(emailPasswordForm.getPassword(), null))) {
            throw new ValidationException("Validation error").withError("password", "incorrect password");
        }
        //check new email
        if (userEmail.equals(emailPasswordForm.getEmail())) {
            throw new ValidationException("Validation error").withError("email", "new email is equivalent to your current email");
        }
        UserEntity existedUser = userRepository.findByEmail(emailPasswordForm.getEmail());
        if (existedUser != null) {
            throw new ValidationException("Validation error").withError("email", "this email already taken");
        }

        user.setNewEmail(emailPasswordForm.getEmail());
        user.setEmailConfirmationToken(UUID.randomUUID().toString().replaceAll("-", ""));

        userRepository.save(user);

        //send email;
        String template = mailerService.loadTemplateFromResource(MailerService.EMAIL_CHANGE_CONFIRMATION_TEMPLATE);
        String letterBody = mailerService.prepareEmailChangeConfirmationTemplate(user.getEmailConfirmationToken(), emailPasswordForm.getEmail(), template);
        mailerService.sendEmail(user.getEmail(), "subjectText", letterBody, true);

    }
}
