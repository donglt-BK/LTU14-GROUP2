package com.bk.olympia.controller;

import com.bk.olympia.model.Lobby;
import com.bk.olympia.model.Message;
import com.bk.olympia.model.entity.Player;
import com.bk.olympia.model.entity.Room;
import com.bk.olympia.model.entity.User;
import com.bk.olympia.model.type.ContentType;
import com.bk.olympia.model.type.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
public class QueueController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(QueueController.class);
    private HashMap<Lobby, Integer> lobbyList = new HashMap<>();

    @MessageMapping("/play/join")
    public void joinQueue(@Payload Message message) {
        //TODO: Get user from DB
        User user = new User();

        Lobby lobby = searchLobby(message.getContent(ContentType.BET_VALUE));

        Message m = new Message(MessageType.JOIN_QUEUE.getValue(), message.getSender());
        m.addContent(ContentType.LOBBY_ID, lobby.getId())
                .addContent(ContentType.LOBBY_PARTICIPANT, lobby.getFirstUser().getName());
        lobby.addUser(user);

        Message.broadcast(lobby.getUsers(), "/queue/play/join", m);
    }

    @MessageMapping("/play/leave")
    public void leaveQueue(@Payload Message message) {
        //TODO: Get user from DB
        User user = new User();
        Lobby lobby = findLobbyById(message.getContent(ContentType.LOBBY_ID));

        Message m = new Message(MessageType.LEAVE_QUEUE.getValue(), message.getSender());
        m.addContent(ContentType.LOBBY_PARTICIPANT, user.getName());
        lobby.removeUser(user);

        Message.broadcast(lobby.getUsers(), "/queue/play/leave", m);
    }

    @MessageMapping("/play/start-game")
    public void startGame(@Payload Message message) {
        //TODO: Get user from DB
        User user = new User();
        ArrayList<Player> players = new ArrayList<>();
        Lobby lobby = findLobbyById(message.getContent(ContentType.LOBBY_ID));

        Message m = new Message(MessageType.START_GAME.getValue(), message.getSender());
        m.addContent(ContentType.START, user.equals(lobby.getFirstUser()));
        Message.broadcast(lobby.getUsers(), "/queue/play/start-game", m);
        lobby.getUsers().forEach(u -> {
            players.add(Player.getInstance(u.getId()));
        });

        if (user.equals(lobby.getFirstUser())) {
            Room room = new Room(lobby.getUsers().size(), lobby.getBetValue(), 10, players);

            m = new Message(MessageType.CREATE_ROOM.getValue(), message.getSender());
            Message.broadcast(lobby.getUsers(), "/queue/play/create-room", m);
            lobbyList.remove(lobby);
        }
    }

    private Lobby searchLobby(int betValue) {
        return lobbyList.entrySet().stream()
                .filter(entry -> entry.getValue() == betValue)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(new Lobby(betValue));
    }

    private Lobby findLobbyById(int id) {
        return lobbyList.entrySet().stream()
                .filter(entry -> entry.getKey().getId() == id)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }
}
