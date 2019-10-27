package com.bk.olympia.controller;

import com.bk.olympia.base.BaseController;
import com.bk.olympia.model.entity.User;
import com.bk.olympia.model.message.Message;
import com.bk.olympia.model.type.ContentType;
import com.bk.olympia.model.type.Destination;
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

//    @RequestMapping(value = "/", method = RequestMethod.GET)
//    @ResponseBody
//    public String test() {
//        return "Test";
//    }

    @MessageMapping("/login")
    @SendToUser(Destination.LOGIN)
    public Message handleLogin(@Payload Message message) {
        User u = validateAccount(message.getContent(ContentType.USERNAME), message.getContent(ContentType.PASSWORD));

        Message m = new Message(MessageType.LOGIN, message.getSender());
        m.addContent(ContentType.USER_ID, u.getId());
        logger.info(String.valueOf((int) m.getContent(ContentType.USER_ID)));
        return m;
    }

    private User validateAccount(String username, String password) {
//        query = entityManager.createQuery("SELECT u FROM User u WHERE username='" + message.getContent().get(ContentType.USERNAME) + "' AND password='" + message.getContent().get(ContentType.PASSWORD) + "'");
//        User user = (User) query.getResultList().get(0);

        User user = userRepository.findByUsernameAndPassword(username, password);
        if (user == null) {
            user = new User(username, password);

//            entityManager.getTransaction().begin();
//            entityManager.persist(user);
//            entityManager.getTransaction().commit();
//            factory.close();
//            entityManager.close();

            userRepository.save(user);
        }
        return user;
    }
}
