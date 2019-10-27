package service;

import com.bk.olympia.model.entity.Room;
import com.bk.olympia.model.entity.User;
import com.bk.olympia.model.message.Message;
import com.bk.olympia.repository.UserList;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

public class MessagingService {
    private static SimpMessagingTemplate template;

    public static void broadcast(List<User> list, String destination, Message message) {
        message.pack();
        list.forEach(u -> template.convertAndSendToUser(u.getUsername(), destination, message));
    }

    public static void broadcast(Room room, String destination, Message message) {
        broadcast(UserList.getUsers(room.getId()), destination, message);
    }

    public static void sendTo(User user, String destination, Message message) {
        message.pack();
        template.convertAndSendToUser(user.getUsername(), destination, message);
    }
}
