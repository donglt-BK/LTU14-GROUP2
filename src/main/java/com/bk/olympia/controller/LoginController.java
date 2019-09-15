package com.bk.olympia.controller;

import com.bk.olympia.model.Message;
import com.bk.olympia.model.type.ContentType;
import com.bk.olympia.model.type.MessageType;
import com.bk.olympia.model.type.MessageDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.HashMap;

@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger("LoginController");

    @MessageMapping("/login")
    @SendTo("/topic/public")
    public Message login(@Payload Message message) {
        System.out.println("HERE");
        HashMap<String, String> contents = new HashMap<>();
        contents.put(ContentType.STATUS, validateAccount(message) ? MessageDetail.LOGIN_SUCCESS : MessageDetail.LOGIN_FAILED);
        logger.info(contents.get(ContentType.STATUS));
        return new Message(MessageType.LOGIN.getValue(), message.getSender(), contents);
    }

    private boolean validateAccount(Message message) {
        return !message.getContent().get(ContentType.USERNAME).equals("") || !message.getContent().get(ContentType.PASSWORD).equals("");
    }
}
