package com.ramotion.roadmap.controllers;

/**
 * Created by Oleg Vasiliev on 19.11.2014.
 * Hold all mappings in one place
 */
public class APIMappings {

    public static class Web {

        public static final String API_ROOT = "/api";
        public static final String GET_SURVEY = "/survey";
        public static final String CREATE_VOTE = "/vote";
        public static final String GET_LANGUAGES = "/languages";
    }

    public static class Socket {

        public static class Subscriptions {
            public static final String TOPIC = "/topic";
            public static final String TOPIC_USER_APPS = TOPIC + "/user_apps";
            public static final String TOPIC_APP_DETAILS = TOPIC + "/app";
            public static final String TOPIC_ERRORS = TOPIC + "/errors";
        }

        public static class Endpoints {
            public static final String NEW_APPLICATION = "/application";
            public static final String NEW_APP_USER = "/application_user_access";
            public static final String NEW_FEATURE = "/feature";
            public static final String NEW_SURVEY = "/survey";
        }

    }

}
