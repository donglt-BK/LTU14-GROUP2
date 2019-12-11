package com.bk.olympia.controller;

import com.bk.olympia.base.BaseController;
import com.bk.olympia.constant.Destination;
import com.bk.olympia.exception.UnauthorizedActionException;
import com.bk.olympia.exception.UserNameNotFoundException;
import com.bk.olympia.model.entity.Player;
import com.bk.olympia.model.entity.User;
import com.bk.olympia.model.message.Message;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import service.HashService;

import javax.persistence.OneToOne;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

@Controller
public class ChatController extends BaseController {
    public Map<Integer, Integer> lobbyUserMap = new HashMap<>();

    @Autowired
    private QueueController queueController;

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
        //User user = findUserById(message.getSender());
        //System.out.println(user.getLobbyId());
        //System.out.println(lobbyId);
        //if (user.getLobbyId() == lobbyId) {
        int userId = message.getSender();
        if (lobbyUserMap.get(userId) == lobbyId) {
            System.out.println("message forward to lobby " + lobbyId + " by " + Destination.LOBBY_CHAT + lobbyId);
            queueController.findLobbyById(lobbyId).getUsers().forEach(user -> System.out.println(userId));
            broadcast(queueController.findLobbyById(lobbyId).getUsers(), Destination.LOBBY_CHAT + lobbyId, message);
        } else throw new UnauthorizedActionException(userId);
    }

    @MessageMapping("/topic/private/room/{roomId}")
    public void processRoomOnlyChat(@DestinationVariable("roomId") int roomId, @Payload Message message) {
        User user = findUserById(message.getSender());
        if (user.getCurrentPlayer().getRoom().getId() == roomId) {
            System.out.println("message forward to lobby " + roomId + " by " + Destination.ROOM_CHAT + roomId);
            broadcast(roomRepository.findById(roomId).getPlayerList().stream().map(Player::getUser).collect(Collectors.toList()), Destination.ROOM_CHAT + roomId, message);
        } else throw new UnauthorizedActionException(user.getId());
    }
}
