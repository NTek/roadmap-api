package com.ramotion.roadmap.controllers;

import com.ramotion.roadmap.config.AppConfig;
import com.ramotion.roadmap.repository.ApplicationRepository;
import com.ramotion.roadmap.repository.UserHasApplicationRepository;
import com.ramotion.roadmap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created by Oleg Vasiliev on 11.11.2014.
 * Controller for testing some things during development
 */
@Controller
@ResponseBody
public class RootController {

    private final long serverStartedAt = System.currentTimeMillis();

    private Environment env;
    private UserRepository userRepository;
    private ApplicationRepository applicationRepository;
    private UserHasApplicationRepository userHasApplicationRepository;

    @Autowired
    public RootController(UserHasApplicationRepository userHasApplicationRepository,
                          UserRepository userRepository, ApplicationRepository applicationRepository, Environment env) {
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.userHasApplicationRepository = userHasApplicationRepository;
        this.env = env;
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Object getStatus() {
        Map<String, Object> response = new HashMap<>();
        response.put("started", AppConfig.DATETIME_FORMATTER.format(new Date(serverStartedAt)));
        response.put("uptime", createUptimeString(System.currentTimeMillis() - serverStartedAt));
        response.put("version", env.getProperty("app.version"));
        return response;
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public Object users() {
        return userRepository.findAll();
    }

    @RequestMapping(value = "/apps", method = RequestMethod.GET)
    public Object applications() {
        return applicationRepository.findAll();
    }

    @RequestMapping(value = "/userapps", method = RequestMethod.GET)
    public Object userapps() {
        return userHasApplicationRepository.findAll();
    }

    private String createUptimeString(long millis) {
        Calendar difference = Calendar.getInstance(TimeZone.getTimeZone("GMT + 00 : 00"));
        difference.setTimeInMillis(millis);
        int seconds = difference.get(Calendar.SECOND);
        int minutes = difference.get(Calendar.MINUTE);
        int hours = difference.get(Calendar.HOUR);
        int days = difference.get(Calendar.DAY_OF_YEAR) - 1;
        int years = difference.get(Calendar.YEAR) - 1970;
        return years + "y. " + days + "d. " + hours + "h. " + minutes + "m. " + seconds + "sec.";
    }
}