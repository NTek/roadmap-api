package com.ramotion.roadmap.service;


import com.ramotion.roadmap.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class MailerService {

    public static final Resource EMAIL_CONFIRMATION_TEMPLATE = new ClassPathResource("email_confirmation_letter_template.html");

    @Autowired
    private JavaMailSender mailSender;

    public String preparePasswordTemplate(String password, String template) {
        if (template == null) return null;
        return template.replaceAll("%password%", password);
    }

    @Async
    public void sendEmail(String receiver, String subject, String body, boolean itsHtml) {
        MimeMessage msg = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, "UTF-8");
        try {
            helper.setFrom(AppConfig.EMAIL_FROM);
            helper.setTo(receiver);
            helper.setSubject(subject);
            helper.setText(body, itsHtml);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        mailSender.send(msg);
    }

    public String loadLetterTemplate(Resource templateResource) {
        try {
            return new String(Files.readAllBytes(templateResource.getFile().toPath()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
