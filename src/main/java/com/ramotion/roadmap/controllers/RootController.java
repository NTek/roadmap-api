package com.ramotion.roadmap.controllers;

import com.ramotion.roadmap.config.AppConfig;
import com.ramotion.roadmap.dto.Greeting;
import com.ramotion.roadmap.model.FeatureEntity;
import com.ramotion.roadmap.repository.ApplicationRepository;
import com.ramotion.roadmap.repository.FeatureRepository;
import com.ramotion.roadmap.repository.UserHasApplicationRepository;
import com.ramotion.roadmap.repository.UserRepository;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    private FeatureRepository featureRepository;
    private ApplicationRepository applicationRepository;
    private UserHasApplicationRepository userHasApplicationRepository;
    private BasicDataSource dataSource;
    private SimpMessagingTemplate template;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    public RootController(UserHasApplicationRepository userHasApplicationRepository,
                          FeatureRepository featureRepository,
                          SimpMessagingTemplate template,
                          BasicDataSource dataSource,
                          UserRepository userRepository, ApplicationRepository applicationRepository, Environment env) {
        this.featureRepository = featureRepository;
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.userHasApplicationRepository = userHasApplicationRepository;
        this.env = env;
        this.template = template;
        this.dataSource = dataSource;
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Object getStatus() {
        Map<String, Object> response = new HashMap<>();
        response.put("started", AppConfig.DATETIME_FORMATTER.format(new Date(serverStartedAt)));
        response.put("uptime", createUptimeString(System.currentTimeMillis() - serverStartedAt));
        response.put("version", env.getProperty("app.version"));
        response.put("database", dataSource.getUrl());
        return response;
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public Object users() {
        return userRepository.findAll();
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public Object login() {
        return userRepository.findByEmailAndPassword("test@test.com", "123");
    }

    @RequestMapping(value = "/apps", method = RequestMethod.GET)
    public Object applications() {
        return applicationRepository.findAll();
    }

    @RequestMapping(value = "/userapps", method = RequestMethod.GET)
    public Object userapps() {
        return userHasApplicationRepository.findAll();
    }

    @RequestMapping(value = "/features", method = RequestMethod.GET)
    public Object features() {
        return featureRepository.findAll();
    }

    @RequestMapping(value = "/createfeature/{id}", method = RequestMethod.GET)
    public Object testCreateFeature(@PathVariable(value = "id") long id,
                                    @RequestParam(value = "name", required = false) String includes) {
        FeatureEntity feature = new FeatureEntity();
        feature.setApplication(applicationRepository.findOne(id));
        featureRepository.save(feature);
        return feature;
    }

    @RequestMapping(value = "/sayhello", method = RequestMethod.GET)
    public String SayHelloToSocket(@RequestParam(value = "name", required = false) String name) {
        this.template.convertAndSend("/topic/greetings", new Greeting(name));
        return "Sent " + name;
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
