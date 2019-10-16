package service;

import com.bk.olympia.model.Message;
import com.bk.olympia.model.entity.User;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

public class MessagingService {
    private static SimpMessagingTemplate template;

    public static void broadcast(List<User> list, String destination, Message message) {
        message.pack();
        list.forEach(u -> template.convertAndSendToUser(u.getUsername(), destination, message));
    }

    public static void sendTo(User user, String destination, Message message) {
        message.pack();
        template.convertAndSendToUser(user.getUsername(), destination, message);
    }
}
