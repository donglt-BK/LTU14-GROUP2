package com.bk.olympia.base;

import com.bk.olympia.model.entity.Room;
import com.bk.olympia.model.entity.User;
import com.bk.olympia.repository.RoomRepository;
import com.bk.olympia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
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

    protected ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
    protected HashMap<Integer, ScheduledFuture> taskQueue = new HashMap<>();

    protected User findUserById(int id) {
//        query = entityManager.createQuery("SELECT u From User u WHERE u.id == " + id);
//        return query.getResultList() != null ? (User) query.getResultList().get(0) : null;

        return userRepository.findById(id);
    }

    protected Room findRoomById(int id) {
        return roomRepository.findById(id);
    }
}
