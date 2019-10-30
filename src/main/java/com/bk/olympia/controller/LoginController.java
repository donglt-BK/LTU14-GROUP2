package com.bk.olympia.controller;

import com.bk.olympia.base.BaseController;
import com.bk.olympia.base.BaseRuntimeException;
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

//    @RequestMapping(value = "/", method = RequestMethod.GET)
//    @ResponseBody
//    public String test() {
//        return "Test";
//    }

    @MessageMapping("/login")
    @SendToUser(Destination.LOGIN)
    public Message handleLogin(Principal principal, @Payload Message message) {
        User u = validateAccount(message.getContent(ContentType.USERNAME), message.getContent(ContentType.PASSWORD));
        u.setUid(principal.getName());

        Message m = new Message(MessageType.LOGIN, message.getSender());
        m.addContent(ContentType.USER_ID, u.getId());
        logger.info("USER LOGIN: " + (int) m.getContent(ContentType.USER_ID));

        userRepository.save(u);
        return m;
    }

    private User validateAccount(String username, String password) throws BaseRuntimeException {
        User user = userRepository.findByUsernameAndPassword(username, password);
        if (user == null) {
            throw new WrongUsernameOrPasswordException();
        }
        return user;
    }

    @MessageExceptionHandler
    @SendToUser(Destination.ERROR)
    public Message handleException(WrongUsernameOrPasswordException e) {
        logger.error(e.getMessage());
        return new ErrorMessage(ErrorType.AUTHENTICATION, e.getUserId());
    }
}
