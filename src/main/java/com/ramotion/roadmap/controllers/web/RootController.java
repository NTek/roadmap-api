package com.ramotion.roadmap.controllers.web;

import com.ramotion.roadmap.config.AppConfig;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created by Oleg Vasiliev on 11.11.2014.
 */
@Controller
@ResponseBody
public class RootController {

    private static final Logger LOG = Logger.getLogger(RootController.class.getName());

    private final long serverStartedAt = System.currentTimeMillis();

    private Environment env;
    private BasicDataSource dataSource;

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    public RootController(Environment env, BasicDataSource dataSource) {
        this.env = env;
        this.dataSource = dataSource;
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Object getStatus() {
        Map<String, Object> response = new HashMap<>();
        response.put("started", AppConfig.DATETIME_FORMATTER.format(new Date(serverStartedAt)));
        response.put("uptime", createUptimeString(System.currentTimeMillis() - serverStartedAt));
        response.put("version", env.getProperty("app.version"));
        response.put("database_used_url", dataSource.getUrl());
        return response;
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
