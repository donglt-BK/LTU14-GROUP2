package com.bk.olympia.base;

import com.bk.olympia.model.entity.Player;
import com.bk.olympia.model.entity.Question;
import com.bk.olympia.model.entity.Room;
import com.bk.olympia.model.entity.User;
import com.bk.olympia.model.message.Message;
import com.bk.olympia.model.type.Destination;
import com.bk.olympia.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

@Controller
public abstract class BaseController {
    @Autowired
    private SimpMessagingTemplate template;
//
//    protected EntityManagerFactory factory = Persistence.createEntityManagerFactory("App");
//    protected EntityManager entityManager = factory.createEntityManager();
//    protected Query query;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected RoomRepository roomRepository;

    @Autowired
    protected TopicRepository topicRepository;

    @Autowired
    protected QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    protected ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
    protected HashMap<Integer, ScheduledFuture> taskQueue = new HashMap<>();

    @MessageExceptionHandler
    @SendToUser(Destination.ERROR)
    public abstract Message handleException(BaseRuntimeException e);

    protected User findUserById(int id) {
//        query = entityManager.createQuery("SELECT u From User u WHERE u.id == " + id);
//        return query.getResultList() != null ? (User) query.getResultList().get(0) : null;

        return userRepository.findById(id);
    }

    protected User findUserByName(String name) {
        return userRepository.findByName(name);
    }

    protected Room findRoomById(int id) {
        return roomRepository.findById(id);
    }

    protected void save(Room room) {
        roomRepository.save(room);
        room.update();
    }

    protected void save(Question question) {
        questionRepository.save(question);
    }

    protected void save(Player player) {

    }

    protected void save(User user) {
        userRepository.save(user);
    }

    public void broadcast(List<User> list, String destination, Message message) {
        message.pack();
        list.forEach(u -> template.convertAndSendToUser(u.getUid(), destination, message));
    }

    public void broadcast(Room room, String destination, Message message) {
        broadcast(UserList.getUsers(room.getId()), destination, message);
    }

    public void sendTo(User user, String destination, Message message) {
        message.pack();
        template.convertAndSendToUser(user.getUid(), destination, message);
    }

//    public void sendToGuest(String username, String destination, Message message) {
//        message.pack();
//        template.convertAndSendToUser(username, destination, message);
//    }

}
