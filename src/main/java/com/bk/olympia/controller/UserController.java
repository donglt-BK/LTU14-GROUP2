package com.bk.olympia.controller;

import com.bk.olympia.base.BaseController;
import com.bk.olympia.model.entity.User;
import com.bk.olympia.model.message.Message;
import com.bk.olympia.model.type.ContentType;
import com.bk.olympia.model.type.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class UserController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @MessageMapping("/user/get-info")
    @SendToUser("/queue/user/get-info")
    public Message handleGetInfo(@Payload Message message) {
        User user = findUserById(message.getSender());
        Message m = new Message(MessageType.GET_INFO, message.getSender());
        m.addContent(ContentType.USER_ID, user.getId())
                .addContent(ContentType.USERNAME, user.getUsername())
                .addContent(ContentType.NAME, user.getName())
                .addContent(ContentType.GENDER, user.getGender())
                .addContent(ContentType.BALANCE, user.getBalance())
                .pack();
        return m;
    }

    @MessageMapping("/user/change-info")
    @SendToUser("/queue/user/change-info")
    public Message handleSetInfo(@Payload Message message) {
        User user = userRepository.getOne(message.getSender());
        message.getContent().forEach((k, v) -> {
            if (k instanceof ContentType) {
                ContentType key = (ContentType) k;

                switch (key) {
                    case NAME:
                        user.setName((String) v);
                        break;
                    case GENDER:
                        user.setGender((int) v);
                        break;
                }
            }
        });
        userRepository.save(user);
        return new Message(MessageType.CHANGE_INFO, message.getSender()).pack();
    }
}
