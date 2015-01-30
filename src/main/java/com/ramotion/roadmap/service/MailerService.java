package com.ramotion.roadmap.service;


import com.ramotion.roadmap.config.AppConfig;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class MailerService {

    private static final Logger LOG = Logger.getLogger(MailerService.class.getName());

    public static final Resource EMAIL_CHANGE_CONFIRMATION_TEMPLATE = new ClassPathResource("email_confirmation_letter_template.html");

    @Autowired
    private Environment env;

    @Autowired
    private JavaMailSender mailSender;

    public String prepareEmailChangeConfirmationTemplate(String confirmationToken, String newEmail, String template) {
        if (template == null) return null;
        String domain = env.getProperty("frontend.domain");
        String path = env.getProperty("frontend.email_change_confirm_page");
        String confirmationLink = domain + path + confirmationToken;
        return template.replaceAll("%confirmationLink%", confirmationLink).replaceAll("%newEmail%", newEmail);
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
        } catch (Exception e) {
            LOG.error("Error while preparing mime message for mailing", e);
            return;
        }
        mailSender.send(msg);
    }

    public String loadTemplateFromResource(Resource templateResource) {
        try {
            return new String(Files.readAllBytes(templateResource.getFile().toPath()));
        } catch (IOException e) {
            LOG.error("Can't load letter template from " + templateResource.getFilename(), e);
            return null;
        }
    }

}
