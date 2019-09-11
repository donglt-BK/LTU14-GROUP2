package com.bk.olympia.controller;

import com.bk.olympia.model.Message;
import com.bk.olympia.model.type.MessageType;
import com.bk.olympia.model.type.Messages;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;

public class LoginController {
    @MessageMapping("/login")
    @SendTo("/topic/public")
    public Message sendMessage(@Payload Message message) {
        return new Message(MessageType.LOGIN.getValue(), message.getSender(), checkAccount(message) ? Messages.LOGIN_SUCCESS : Messages.LOGIN_FAILED);
    }

    private boolean checkAccount(Message message) {
        return message.getContent().length() > 0;
    }
}
