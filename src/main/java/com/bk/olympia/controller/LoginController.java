package com.bk.olympia.controller;

import com.bk.olympia.base.BaseController;
import com.bk.olympia.constant.ContentType;
import com.bk.olympia.constant.Destination;
import com.bk.olympia.constant.MessageType;
import com.bk.olympia.exception.*;
import com.bk.olympia.model.entity.User;
import com.bk.olympia.model.message.Message;
import com.bk.olympia.model.message.MessageAccept;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class LoginController extends BaseController {

    @Override
    protected void init() {
        this.logger = LoggerFactory.getLogger(LoginController.class);
    }

    @MessageMapping("/auth/login")
    @SendToUser(Destination.LOGIN)
    public Message processLogin(Principal principal, @Payload Message message) {
        User u = validateAccount(message.getContent(ContentType.USERNAME), message.getContent(ContentType.PASSWORD));
        return handleLogin(principal, u);
    }

    private Message handleLogin(Principal principal, User user) {
        user.setUid(principal.getName());
        Message m = new Message(MessageType.LOGIN, user.getId());
        m.addContent(ContentType.USER_ID, user.getId());
        logger.info("USER LOGIN: " + (int) m.getContent(ContentType.USER_ID));

        userRepository.save(user);
        return m;
    }

    private User validateAccount(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password).orElseGet(() -> {
            throw new WrongUsernameOrPasswordException();
        });
    }

    @MessageMapping("/auth/sign_up")
    @SendToUser(Destination.SIGN_UP)
    public Message processSignUp(Principal principal, @Payload Message message) {
        String username = message.getContent(ContentType.USERNAME);
        String password = message.getContent(ContentType.PASSWORD);
        String name = message.getContent(ContentType.NAME);
        int gender = message.getContent(ContentType.GENDER);
        return handleSignUp(principal, username, password, name, gender);
    }

    private Message handleSignUp(Principal principal, String username, String password, String name, int gender) {
        if (!userRepository.findByUsername(username).isPresent()) {
            if (!userRepository.findByName(name).isPresent()) {
                if (password != null && !password.trim().isEmpty()) {
                    if (name != null && !name.trim().isEmpty()) {
                        User user = new User(username, password, name, gender);
                        user.setUid(principal.getName());
                        save(user);
                        logger.info("Player " + name + " signed up successfully!");
                        return new MessageAccept(MessageType.SIGN_UP, user.getId());
                    } else throw new NameCannotBeNullException();
                } else throw new PasswordCannotBeNullException();
            } else throw new NameAlreadyTakenException();
        } else throw new UsernameAlreadyTakenException();
    }
}
