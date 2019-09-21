package com.bk.olympia.base;

import com.bk.olympia.model.Message;
import com.bk.olympia.model.entity.User;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

public class Broadcaster {
    private static SimpMessagingTemplate template;
    public static void broadcast(List<User> list, String destination, Message message) {
        list.forEach(u -> template.convertAndSendToUser(u.getName(), destination, message));
    }
}
