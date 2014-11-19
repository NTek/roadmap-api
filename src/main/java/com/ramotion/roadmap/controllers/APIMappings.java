package com.ramotion.roadmap.controllers;

/**
 * Created by Oleg Vasiliev on 19.11.2014.
 * Hold all mappings in one place
 */
public class APIMappings {

    public static class Web {

        public static final String GET_APP_SURVEYS = "";
        public static final String GET_SURVEY_FEATURES = "";
        public static final String VOTE = "";

    }

    public static class Socket {

        public static class Subscriptions {
            public static final String APPLICATIONS = "";
            public static final String SURVEYS = "";
            public static final String VOTES = "";
        }

        public static class Endpoints {
            public static final String NEW_APPLICATION = "";
            public static final String NEW_FEATURE = "";
            public static final String NEW_SURVEY = "";
        }

    }

}
