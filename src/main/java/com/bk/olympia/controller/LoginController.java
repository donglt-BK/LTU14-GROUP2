package com.bk.olympia.controller;

import com.bk.olympia.base.BaseController;
import com.bk.olympia.base.BaseRuntimeException;
import com.bk.olympia.exception.NameCannotBeNullException;
import com.bk.olympia.exception.PasswordCannotBeNullException;
import com.bk.olympia.exception.UsernameAlreadyTakenException;
import com.bk.olympia.exception.WrongUsernameOrPasswordException;
import com.bk.olympia.model.entity.User;
import com.bk.olympia.model.message.ErrorMessage;
import com.bk.olympia.model.message.Message;
import com.bk.olympia.model.type.ContentType;
import com.bk.olympia.model.type.Destination;
import com.bk.olympia.model.type.ErrorType;
import com.bk.olympia.model.type.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class LoginController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger("LoginController");

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
        User user = userRepository.findByUsernameAndPassword(username, password);
        if (user == null) {
            throw new WrongUsernameOrPasswordException();
        }
        return user;
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
        if (userRepository.findByUsername(username) == null) {
            if (password != null && !password.trim().isEmpty()) {
                if (name != null && !name.trim().isEmpty()) {
                    User user = new User(username, password, name, gender);
                    return handleLogin(principal, user);
                }
                throw new NameCannotBeNullException();
            }
            throw new NameCannotBeNullException();
        } else throw new UsernameAlreadyTakenException();
    }

    @MessageExceptionHandler
    @SendToUser(Destination.ERROR)
    public Message handleException(BaseRuntimeException e) {
        logger.error(e.getMessage());
        if (e instanceof WrongUsernameOrPasswordException)
            return new ErrorMessage(ErrorType.AUTHENTICATION, e.getUserId());
        else if (e instanceof UsernameAlreadyTakenException)
            return new ErrorMessage(ErrorType.USERNAME_ALREADY_TAKEN, e.getUserId());
        else if (e instanceof PasswordCannotBeNullException)
            return new ErrorMessage(ErrorType.PASSWORD_IS_NULL, e.getUserId());
        else if (e instanceof NameCannotBeNullException)
            return new ErrorMessage(ErrorType.NAME_IS_NULL, e.getUserId());
        return null;
    }
}
