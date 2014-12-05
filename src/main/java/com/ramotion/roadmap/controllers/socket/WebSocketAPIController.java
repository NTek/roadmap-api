package com.ramotion.roadmap.controllers.socket;

import com.ramotion.roadmap.controllers.APIMappings;
import com.ramotion.roadmap.service.WebsocketAPIService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;

/**
 * Created by Oleg Vasiliev on 04.12.2014.
 */
@Controller
public class WebSocketAPIController {

    private static final Logger LOG = Logger.getLogger(WebSocketAPIController.class.getName());

    private SimpMessagingTemplate template;
    private WebsocketAPIService websocketAPIService;

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    public WebSocketAPIController(SimpMessagingTemplate template, WebsocketAPIService websocketAPIService) {
        this.template = template;
        this.websocketAPIService = websocketAPIService;
    }

    //Creating-editing user applications, result delivered to user specified endpoint and maybe to edited app's endpoint
    @MessageMapping(APIMappings.Socket.Endpoints.NEW_APPLICATION)
    @SendToUser(APIMappings.Socket.Subscriptions.TOPIC_USER_APPS)
    public List<Object> applcation(Principal principal) throws Exception {

        template.convertAndSend(APIMappings.Socket.Subscriptions.TOPIC_APP_DETAILS + "/" + "app-token");
        return null;
    }

    @MessageMapping(APIMappings.Socket.Endpoints.NEW_FEATURE)
    public void feature(Object inputObject, Principal principal) throws Exception {

        // Result sends to specified app endpoint
        // Maybe should make this on service layer
        template.convertAndSend(APIMappings.Socket.Subscriptions.TOPIC_APP_DETAILS + "/" + "app-token");
    }

    @MessageMapping(APIMappings.Socket.Endpoints.NEW_SURVEY)
    public void survey(Object inputObject, Principal principal) throws Exception {

        // Result sends to specified app endpoint
        // Maybe should make this on service layer
        template.convertAndSend(APIMappings.Socket.Subscriptions.TOPIC_APP_DETAILS + "/" + "app-token");
    }

    /**
     * Handling exceptions to send errors to user specified message broker
     */
    @MessageExceptionHandler
    @SendToUser(APIMappings.Socket.Subscriptions.TOPIC_ERRORS)
    public String handleException(Throwable exception) {

        return exception.getMessage();
    }

}
