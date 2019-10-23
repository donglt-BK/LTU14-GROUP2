package com.bk.olympia.controller;

import com.bk.olympia.base.BaseController;
import com.bk.olympia.model.Lobby;
import com.bk.olympia.model.entity.Player;
import com.bk.olympia.model.entity.Room;
import com.bk.olympia.model.entity.User;
import com.bk.olympia.model.message.ErrorMessage;
import com.bk.olympia.model.message.Message;
import com.bk.olympia.model.message.MessageAccept;
import com.bk.olympia.model.type.ContentType;
import com.bk.olympia.model.type.ErrorType;
import com.bk.olympia.model.type.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import service.MessagingService;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

@Controller
public class QueueController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(QueueController.class);
    private TreeMap<Lobby, Integer> lobbyList = new TreeMap<>();

//    @MessageMapping("/play/get-lobby-list")
//    public void getLobbyList(@Payload Message message) {
//        User user = findUserById(message.getSender());
//        Message m = new Message(MessageType.GET_LOBBY_LIST, message.getSender());
//        lobbyList.forEach((lobby, betValue) -> m.addContent(ContentType.LOBBY_ID, lobby.getId())
//                .addContent(ContentType.LOBBY_NAME, lobby.getName())
//                .addContent(ContentType.LOBBY_PARTICIPANT, lobby.getUsers().size())
//                .addContent(ContentType.BET_VALUE, betValue));
//        MessagingService.sendTo(user, "/queue/play/get-lobby-list", m);
//    }
//
//    @MessageMapping("/play/create-lobby")
//    public void createLobby(@Payload Message message) {
//        User user = findUserById(message.getSender());
//        Lobby lobby = new Lobby(message.getContent(ContentType.BET_VALUE));
//        user.join(lobby);
//        lobbyList.put(lobby, lobby.getBetValue());
//    }
//
//    @MessageMapping("/play/quick-join")
//    public void quickJoin(@Payload Message message) {
//        User user = findUserById(message.getSender());
//
//        Lobby lobby = searchLobby(message.getContent(ContentType.BET_VALUE));
//        if (lobby != null)
//            user.join(lobby);
//        else {
//
//        }
//    }
//
//    @MessageMapping("play/join")
//    public void joinLobby(@Payload Message message) {
//        User user = findUserById(message.getSender());
//
//        Lobby lobby = findLobbyById(message.getContent(ContentType.LOBBY_ID));
//        if (lobby != null)
//            user.join(lobby);
//        else {
//
//        }
//    }

    @MessageMapping("/play/join")
    public void handleFindLobby(@Payload Message message) {
        User user = findUserById(message.getSender());
        MessagingService.sendTo(user, "/queue/play/join", new MessageAccept(MessageType.JOIN_LOBBY, message.getSender()));

        Lobby lobby = findLobbyByBetValue(message.getContent(ContentType.BET_VALUE));
        lobby.addUser(user);

        broadcastLobbyInfo(message, lobby);
    }

    @MessageMapping("/play/invite")
    public void handleInvite(@Payload Message message) {
        User user = findUserById(message.getSender());
        User recipient = findUserByName(message.getContent(ContentType.NAME));

        int betValue = message.getContent(ContentType.BET_VALUE);
        if (recipient.getBalance() >= betValue)
            MessagingService.sendTo(recipient, "/queue/play/invite", message);
        else
            MessagingService.sendTo(user, "queue/error", new ErrorMessage(ErrorType.INVALID_INVITE, message.getSender()));
    }

    @MessageMapping("/play/invite-rep")
    public void handleReplyInvite(@Payload Message message) {
        User user = findUserById(message.getSender());
        User recipient = findUserByName(message.getContent(ContentType.NAME));

        MessagingService.sendTo(recipient, "queue/play/invite", message);
        if (message.getContent(ContentType.REPLY)) {
            Lobby lobby = new Lobby(message.getContent(ContentType.BET_VALUE));
            lobby.addUser(recipient)
                    .addUser(user);
            broadcastLobbyInfo(message, lobby);
        }
    }

    @MessageMapping("/play/leave")
    public void handleLeaveLobby(@Payload Message message) {
        User user = findUserById(message.getSender());
        Lobby lobby = findLobbyById(message.getContent(ContentType.LOBBY_ID));

        Message m = new Message(MessageType.LEAVE_LOBBY, message.getSender());
        m.addContent(ContentType.LOBBY_PARTICIPANT, user.getName());
        lobby.removeUser(user);

        MessagingService.broadcast(lobby.getUsers(), "/queue/play/leave", m);
    }

    @MessageMapping("/play/start-game")
    public void handleStartGame(@Payload Message message) {
        User user = findUserById(message.getSender());
        ArrayList<Player> players = new ArrayList<>();
        Lobby lobby = findLobbyById(message.getContent(ContentType.LOBBY_ID));

        boolean canStart = user.equals(lobby.getHost());
        Message m = new Message(MessageType.START_GAME, message.getSender());
        m.addContent(ContentType.START, canStart);
        MessagingService.sendTo(user, "/queue/play/start-game", m);

        lobby.getUsers().forEach(u -> {
            players.add(Player.getInstance(u.getId(), lobby.getBetValue()));
        });

        if (canStart) {
            Room room = new Room(lobby.getId(), lobby.getBetValue(), players);
//            entityManager.persist(room);

            roomRepository.save(room);

            m = new Message(MessageType.CREATE_ROOM, message.getSender());
            m.addContent(ContentType.ROOM_ID, room.getId());
            MessagingService.broadcast(lobby.getUsers(), "/queue/play/create-room", m);
            removeLobby(lobby);
        }
    }

    private Lobby findLobbyByBetValue(int betValue) {
        return lobbyList.entrySet().stream()
                .filter(entry -> entry.getValue() == betValue)
                .map(Map.Entry::getKey)
                .findAny()
                .orElse(new Lobby(betValue));
    }

    private Lobby findLobbyById(int id) {
        return lobbyList.keySet().stream()
                .filter(integer -> integer.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private void broadcastLobbyInfo(Message message, Lobby lobby) {
        Message m = new Message(MessageType.JOIN_LOBBY, message.getSender());
        m.addContent(ContentType.LOBBY_ID, lobby.getId())
                .addContent(ContentType.LOBBY_NAME, lobby.getName())
                .addContent(ContentType.LOBBY_PARTICIPANT, lobby.getUsers());
        MessagingService.broadcast(lobby.getUsers(), "/queue/play/join", m);
    }

    private void removeLobby(Lobby lobby) {
        Lobby.addDeletedId(lobby.getId());
        lobbyList.remove(lobby);
    }
}