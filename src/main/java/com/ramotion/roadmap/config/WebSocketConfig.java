package com.ramotion.roadmap.config;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import javax.annotation.PostConstruct;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    private static final Logger LOG = Logger.getLogger(WebSocketConfig.class.getName());

    public static final String INITIAL_CONNECT = "/socket";
    public static final String TOPIC = "/topic";
    public static final String TOPIC_APPS = TOPIC + "/apps";

    @PostConstruct
    public void postConstruct() {
        LOG.info("WebSocket config constructed");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //Message Brokers for subscribers
        registry.enableSimpleBroker(TOPIC);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(INITIAL_CONNECT).withSockJS();
    }

}
