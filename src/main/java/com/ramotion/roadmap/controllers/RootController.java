package com.ramotion.roadmap.controllers;

import com.ramotion.roadmap.config.AppConfig;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.*;

@Controller
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
    public Object getStatus(Principal principal, HttpServletRequest req) {
//        Enumeration<String> headers = req.getHeaderNames();
        Map<String, Object> response = new HashMap<>();
        response.put("started", AppConfig.DATETIME_FORMATTER.format(new Date(serverStartedAt)));
        response.put("uptime", createUptimeString(System.currentTimeMillis() - serverStartedAt));
        response.put("version", env.getProperty("app.version"));
        response.put("database_used_url", dataSource.getUrl());

        if (principal != null) {
            response.put("logged_in_as", principal.getName());
        }
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

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getStatus(Principal principal, HttpServletResponse res) {
        if (principal == null) res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return "login";
    }
}
