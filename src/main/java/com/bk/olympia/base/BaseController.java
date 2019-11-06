package com.bk.olympia.base;

import com.bk.olympia.constant.Destination;
import com.bk.olympia.constant.ErrorType;
import com.bk.olympia.exception.*;
import com.bk.olympia.model.entity.*;
import com.bk.olympia.model.message.ErrorMessage;
import com.bk.olympia.model.message.Message;
import com.bk.olympia.repository.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

@Controller
public abstract class BaseController {
    protected Logger logger;

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

    @PostConstruct
    protected abstract void init();

    @MessageExceptionHandler
    @SendToUser(Destination.ERROR)
    protected ErrorMessage handleException(BaseRuntimeException e) {
        logger.error(e.getMessage());
        if (e instanceof WrongUsernameOrPasswordException)
            return new ErrorMessage(ErrorType.AUTHENTICATION, e.getUserId());
        else if (e instanceof UsernameAlreadyTakenException)
            return new ErrorMessage(ErrorType.USERNAME_ALREADY_TAKEN, e.getUserId());
        else if (e instanceof PasswordCannotBeNullException)
            return new ErrorMessage(ErrorType.PASSWORD_IS_NULL, e.getUserId());
        else if (e instanceof NameCannotBeNullException)
            return new ErrorMessage(ErrorType.NAME_IS_NULL, e.getUserId());
        else if (e instanceof InsufficientBalanceException)
            return new ErrorMessage(ErrorType.INSUFFICIENT_BALANCE, e.getUserId());
        else if (e instanceof UserNameNotFoundException)
            return new ErrorMessage(ErrorType.WRONG_NAME, e.getUserId());
        else if (e instanceof TargetInsufficientBalanceException)
            return new ErrorMessage(ErrorType.TARGET_INSUFFICIENT_BALANCE, e.getUserId());
        else if (e instanceof UnauthorizedActionException)
            return new ErrorMessage(ErrorType.INVALID_ACTION, e.getUserId());
        else if (e instanceof AnswerPlacingViolationException)
            return new ErrorMessage(ErrorType.ANSWER_PLACING_VIOLATION, e.getUserId());
        return null;
    }

    protected User findUserById(int id) {
//        query = entityManager.createQuery("SELECT u From User u WHERE u.id == " + id);
//        return query.getResultList() != null ? (User) query.getResultList().get(0) : null;

        return userRepository.findById(id);
    }

    protected User findUserByName(String name) {
        return userRepository.findByName(name).orElse(null);
    }

    protected Room findRoomById(int id) {
        return roomRepository.findById(id);
    }

    protected Topic findTopicById(int id) {
        return topicRepository.findById(id);
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

    protected void broadcast(String destination, Message message) {
        assert destination.startsWith("/topic/");
        template.convertAndSend(destination, message);
    }

    protected void broadcast(List<User> list, String destination, Message message) {
        message.pack();
        list.forEach(u -> template.convertAndSendToUser(u.getUid(), destination, message));
    }

    protected void broadcast(Room room, String destination, Message message) {
        broadcast(UserList.getUsers(room.getId()), destination, message);
    }

    protected void sendTo(User user, String destination, Message message) {
        message.pack();
        template.convertAndSendToUser(user.getUid(), destination, message);
    }
}
