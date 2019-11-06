package com.bk.olympia.controller;

import com.bk.olympia.base.BaseController;
import com.bk.olympia.constant.Destination;
import com.bk.olympia.exception.UnauthorizedActionException;
import com.bk.olympia.exception.UserNameNotFoundException;
import com.bk.olympia.model.entity.User;
import com.bk.olympia.model.message.Message;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController extends BaseController {

    @Override
    protected void init() {
        this.logger = LoggerFactory.getLogger(ChatController.class);
    }

    @MessageMapping("/topic/public")
    @SendTo(Destination.PUBLIC_CHAT)
    public Message processPublicChat(@Payload Message message) {
        return message;
    }

    @MessageMapping("/topic/private/user/{userName}")
    public void processPrivateChat(@DestinationVariable("userName") String name, @Payload Message message) {
        User user = findUserByName(name);
        try {
            broadcast(Destination.PRIVATE_CHAT + user.getId(), message);
        } catch (NullPointerException e) {
            throw new UserNameNotFoundException(user.getId());
        }
    }

    @MessageMapping("/topic/private/lobby/{lobbyId}")
    public void processLobbyOnlyChat(@DestinationVariable("lobbyId") int lobbyId, @Payload Message message) {
        User user = findUserById(message.getSender());
        if (user.getLobbyId() == lobbyId)
            broadcast(Destination.LOBBY_CHAT + lobbyId, message);
        else throw new UnauthorizedActionException(user.getId());
    }

    @MessageMapping("/topic/private/room/{roomId}")
    public void processRoomOnlyChat(@DestinationVariable("roomId") int roomId, @Payload Message message) {
        User user = findUserById(message.getSender());
        if (user.getCurrentPlayer().getRoom().getId() == roomId)
            broadcast(Destination.ROOM_CHAT + roomId, message);
        else throw new UnauthorizedActionException(user.getId());
    }
}
