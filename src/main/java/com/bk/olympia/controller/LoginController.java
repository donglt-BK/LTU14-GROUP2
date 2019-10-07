package com.bk.olympia.controller;

import com.bk.olympia.model.Message;
import com.bk.olympia.model.entity.User;
import com.bk.olympia.model.type.ContentType;
import com.bk.olympia.model.type.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger("LoginController");

//    @RequestMapping(value = "/", method = RequestMethod.GET)
//    @ResponseBody
//    public String test() {
//        return "Test";
//    }

    @MessageMapping("/login")
    @SendToUser("/queue/login")
    public Message login(@Payload Message message) {
        User u = validateAccount(message);

        Message m = new Message(MessageType.LOGIN, message.getSender());
        m.addContent(ContentType.USER_ID, u.getId());
        logger.info((String) m.getContent().get(ContentType.USER_ID));
        return m;
    }

    private User validateAccount(Message message) {
        query = entityManager.createQuery("SELECT u FROM User u WHERE username='" + message.getContent().get(ContentType.USERNAME) + "' && password='" + message.getContent().get(ContentType.PASSWORD) + "'");
        User user = (User) query.getResultList().get(0);
        if (user == null) {
            user = new User((String) message.getContent().get(ContentType.USERNAME), (String) message.getContent().get(ContentType.PASSWORD));
            user.setName("player" + user.getId());
            user.setGender(User.Gender.MALE.getValue());

//            entityManager.createNativeQuery("INSERT INTO User VALUES (?, ?, ?, ?, ?, ?)")
//                    .setParameter(1, user.getId())
//                    .setParameter(2, user.getUsername())
//                    .setParameter(3, user.getPassword())
//                    .setParameter(4, user.getName())
//                    .setParameter(5, user.getGender())
//                    .setParameter(6, user.getBalance())
//                    .executeUpdate();
            entityManager.getTransaction().begin();
            entityManager.persist(user);
            entityManager.getTransaction().commit();
            factory.close();
            entityManager.close();
        }
        return user;
    }
}
