package com.ramotion.roadmap.utils;

public class APIMappings {

    public static class Web {
        public static final String SDK_API_ROOT = "/api";

        public static final String SDK_GET_SURVEY = "/survey";
        public static final String SDK_CREATE_VOTE = "/vote";
        public static final String SDK_GET_LANGUAGES = "/languages";

        public static final String FRONTEND_REGISTRATION = "/register";
        public static final String FRONTEND_REGISTRATION_CONFIRM = "/confirm";
        public static final String FRONTEND_PROFILE = "/profile";
        public static final String FRONTEND_APPS = "/apps";
        public static final String FRONTEND_SURVEY = "/survey";

    }

    public static class Socket {
        public static final String INITIAL_CONNECT = "/socket";
        public static final String TOPIC = "/topic";
        public static final String TOPIC_APPS = TOPIC + "/apps";

    }

}
