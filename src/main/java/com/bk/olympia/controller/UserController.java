package com.bk.olympia.controller;

import com.bk.olympia.base.BaseController;
import com.bk.olympia.base.BaseRuntimeException;
import com.bk.olympia.event.DisconnectUserFromLobbyEvent;
import com.bk.olympia.event.DisconnectUserFromRoomEvent;
import com.bk.olympia.model.History;
import com.bk.olympia.model.entity.Player;
import com.bk.olympia.model.entity.Question;
import com.bk.olympia.model.entity.Room;
import com.bk.olympia.model.entity.User;
import com.bk.olympia.model.message.ErrorMessage;
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
import service.SpringEventService;

import java.util.ArrayList;

@Controller
public class UserController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @MessageMapping("/user/get-info")
    @SendToUser(Destination.GET_USER_INFO)
    public Message processGetInfo(@Payload Message message) {
        User user = findUserById(message.getSender());
        return handleGetInfo(user);
    }

    private Message handleGetInfo(User user) {
        Message m = new Message(MessageType.GET_INFO, user.getId());
        m.addContent(ContentType.USER_ID, user.getId())
                .addContent(ContentType.USERNAME, user.getUsername())
                .addContent(ContentType.NAME, user.getName())
                .addContent(ContentType.GENDER, user.getGender())
                .addContent(ContentType.BALANCE, user.getBalance())
                .pack();
        return m;
    }

    @MessageMapping("/user/change-info")
    @SendToUser(Destination.CHANGE_USER_INFO)
    public Message processSetInfo(@Payload Message message) {
        User user = userRepository.getOne(message.getSender());
        return handleSetInfo(user, message);
    }

    private Message handleSetInfo(User user, Message message) {
        message.getContent().forEach((k, v) -> {
            if (k instanceof ContentType) {
                ContentType key = (ContentType) k;

                switch (key) {
                    case NAME:
                        user.setName((String) v);
                        break;
                    case GENDER:
                        user.setGender((int) v);
                        break;
                }
            }
        });
        userRepository.save(user);
        return new Message(MessageType.CHANGE_INFO, message.getSender()).pack();
    }

    @MessageMapping("/user/get-recent-history")
    @SendToUser(Destination.GET_RECENT_HISTORY)
    public Message processGetHistory(@Payload Message message) {
        User user = findUserById(message.getSender());
        return handleGetHistory(user);
    }

    private Message handleGetHistory(User user) {
        ArrayList<Player> playerHistory = (ArrayList<Player>) user.getPlayerList();
        ArrayList<Room> roomHistory = new ArrayList<>();
        playerHistory.forEach(p -> roomHistory.add(p.getRoom()));
        Message m = new Message(MessageType.GET_RECENT_HISTORY, user.getId());

        ArrayList<History> recentHistories = new ArrayList<>();
        roomHistory.forEach(room -> {
            recentHistories.add(new History(room, user));
        });
        m.addContent(ContentType.HISTORY, recentHistories);
        return m;
    }

    @MessageMapping("/user/add-question")
    @SendToUser(Destination.ADD_QUESTION)
    public Message processAddQuestion(@Payload Message message) {
        User user = findUserById(message.getSender());
        return handleAddQuestion(user, message.getContent(ContentType.QUESTION), message.getContent(ContentType.ANSWER));
    }

    private Message handleAddQuestion(User user, String question, String[] answers) {
        //TODO: Add question and check if needed
        questionRepository.save(new Question());
        return null;
    }

    @MessageMapping("/user/logout")
    @SendToUser(Destination.LOGOUT)
    public Message processLogout(@Payload Message message) {
        User user = findUserById(message.getSender());
        return handleLogout(user);
    }

    private Message handleLogout(User user) {
        //TODO: Disconnect user from active lobby
        if (user.getLobbyId() >= 0)
            SpringEventService.publishEvent(new DisconnectUserFromLobbyEvent(this, user));
            //TODO: Disconnect user from active room
        else {
            SpringEventService.publishEvent(new DisconnectUserFromRoomEvent(this, user));
        }
        return null;
    }

    @Override
    public ErrorMessage handleException(BaseRuntimeException e) {
        return null;
    }
}
