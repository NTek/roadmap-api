package com.ramotion.roadmap.controllers.socket;

import com.ramotion.roadmap.dto.ChatMessage;
import com.ramotion.roadmap.dto.MessageText;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;

/**
 * Created by Oleg Vasiliev on 14.11.2014.
 */
@Controller
public class WebSocketController {

//    /**
//     * Request - response mapping without message broker
//     */
//    @SubscribeMapping("/")
//    public ChatMessage subscribeToApplications() {
//        return new ChatMessage("Languages list!", "System");
//    }

    /**
     * Request(with data) without response - responce sent to
     * message broker, client must be subscribed to message broker for receive responce
     */
    @MessageMapping("/chat")
//    @SendTo("/topic/chat")
    @SendToUser(value = "/topic/chat")
    public ChatMessage message(MessageText text, Principal principal) throws Exception {
        return new ChatMessage(text.getText(), principal.getName());
    }

//    /**
//     * Handling exceptions to send errors to error message broker
//     */
//    @MessageExceptionHandler
//    @SendToUser("/topic/errors")
//    public String handleException(Throwable exception) {
//        return exception.getMessage();
//    }

}
