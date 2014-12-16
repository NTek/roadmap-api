package com.ramotion.roadmap.controllers;

import com.ramotion.roadmap.dto.ChatMessage;
import com.ramotion.roadmap.dto.MessageText;
import com.ramotion.roadmap.model.ApplicationEntity;
import com.ramotion.roadmap.service.ApplicationService;
import com.ramotion.roadmap.utils.APIMappings;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;

@Controller
public class WebSocketAPIController {

    private static final Logger LOG = Logger.getLogger(WebSocketAPIController.class.getName());

    @Autowired
    private ApplicationService applicationService;

    @MessageMapping("/chat")
    @SendTo(APIMappings.Socket.TOPIC + "/chat")
//    @SendToUser(value = "/topic/chat")
    public ChatMessage message(MessageText text, Principal principal) throws Exception {
        return new ChatMessage(text.getText(), principal.getName());
    }

    @SubscribeMapping("/apps")
    public List<ApplicationEntity> getUserApplications(Principal principal) {
        List<ApplicationEntity> userApplications = applicationService.getApplicationsByUser(principal.getName());
        return userApplications;
    }

//    @MessageMapping(APIMappings.Socket.Endpoints.NEW_APPLICATION)
//    @SendToUser(APIMappings.Socket.Subscriptions.TOPIC_USER_APPS)
//    public List<Object> applcation(ApplicationEntity request, Principal principal) throws Exception {
//        ApplicationEntity app = websocketAPIService.createOrUpdateApplication(request);
//        websocketUpdateService.updateAppDetails(app.getId());
//        return null;
//    }

//    /**
//     * Handling exceptions to send errors to user specified message broker
//     */
//    @MessageExceptionHandler
//    @SendToUser(APIMappings.Socket.Subscriptions.TOPIC_ERRORS)
//    public String handleException(Throwable exception) {
//        //TODO: Design errors output
//        return exception.getMessage();
//    }

}
