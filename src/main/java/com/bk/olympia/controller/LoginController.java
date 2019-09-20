package com.bk.olympia.controller;

import com.bk.olympia.model.entity.Message;
import com.bk.olympia.model.entity.User;
import com.bk.olympia.model.type.ContentType;
import com.bk.olympia.model.type.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class LoginController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger("LoginController");

    @MessageMapping("/login")
    @SendToUser("/queue/login")
    public Message login(@Payload Message message) {
        User u = validateAccount(message);

        Message m = new Message(MessageType.LOGIN.getValue(), message.getSender());
        m.addContent(ContentType.USER_ID, u != null ? u.getId() : -1);
        logger.info((String) m.getContent().get(ContentType.USER_ID));
        return m;
    }

    private User validateAccount(Message message) {
        query = manager.createQuery("SELECT " + message.getSender() + " FROM 'User'");
        return (User) query.getResultList().get(0);
    }
}
