package com.bk.olympia.controller;

import com.bk.olympia.base.BaseController;
import com.bk.olympia.base.BaseRuntimeException;
import com.bk.olympia.event.DisconnectUserFromLobbyEvent;
import com.bk.olympia.exception.InsufficientBalanceException;
import com.bk.olympia.exception.InvalidActionException;
import com.bk.olympia.exception.TargetInsufficientBalanceException;
import com.bk.olympia.model.Lobby;
import com.bk.olympia.model.entity.Player;
import com.bk.olympia.model.entity.Room;
import com.bk.olympia.model.entity.User;
import com.bk.olympia.model.message.Message;
import com.bk.olympia.model.message.MessageAccept;
import com.bk.olympia.model.type.ContentType;
import com.bk.olympia.model.type.Destination;
import com.bk.olympia.model.type.MessageType;
import com.bk.olympia.repository.UserList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import service.MessagingService;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

@Controller
public class QueueController extends BaseController implements ApplicationListener<ApplicationEvent> {
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


    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof DisconnectUserFromLobbyEvent) {
            handleLeaveLobby(((DisconnectUserFromLobbyEvent) event).getUser(), findLobbyById(((DisconnectUserFromLobbyEvent) event).getLobbyId()));
        }
    }

    @MessageMapping("/play/join")
    public void processFindLobby(@Payload Message message) throws BaseRuntimeException {
        User user = findUserById(message.getSender());
        int betValue = message.getContent(ContentType.BET_VALUE);

        handleFindLobby(user, betValue);
    }

    private void handleFindLobby(User user, int betValue) throws BaseRuntimeException {
        if (betValue > user.getBalance())
            throw new InsufficientBalanceException(user.getId());

        MessagingService.sendTo(user, Destination.FIND_LOBBY, new MessageAccept(MessageType.JOIN_LOBBY, user.getId()));

        Lobby lobby = findLobbyByBetValue(betValue);
        lobby.addUser(user);

        broadcastLobbyInfo(user.getId(), lobby);
    }

    @MessageMapping("/play/invite")
    public void processInvite(@Payload Message message) throws BaseRuntimeException {
        User user = findUserById(message.getSender());
        User recipient = findUserByName(message.getContent(ContentType.NAME));
        int betValue = message.getContent(ContentType.BET_VALUE);

        handleInvite(user, recipient, betValue, message);
    }

    private void handleInvite(User user, User recipient, int betValue, Message message) throws BaseRuntimeException {
        if (betValue > user.getBalance())
            throw new InsufficientBalanceException(user.getId());
        if (recipient.getBalance() >= betValue)
            MessagingService.sendTo(recipient, Destination.INVITE_PLAYER, message);
        else throw new TargetInsufficientBalanceException(user.getId());
    }

    @MessageMapping("/play/invite-rep")
    public void processReplyInvite(@Payload Message message) {
        User user = findUserById(message.getSender());
        User recipient = findUserByName(message.getContent(ContentType.NAME));

        handleReplyInvite(user, recipient, message);
    }

    private void handleReplyInvite(User user, User recipient, Message message) {
        MessagingService.sendTo(recipient, Destination.INVITE_PLAYER, message);
        if (message.getContent(ContentType.REPLY)) {
            Lobby lobby = new Lobby(message.getContent(ContentType.BET_VALUE));
            lobby.addUser(recipient)
                    .addUser(user);
            broadcastLobbyInfo(user.getId(), lobby);
        }
    }

    @MessageMapping("/play/change-info")
    public void processChangeLobbyInfo(@Payload Message message) throws BaseRuntimeException {
        User user = findUserById(message.getSender());
        Lobby lobby = findLobbyById(message.getContent(ContentType.LOBBY_ID));

        handleChangeLobbyInfo(user, lobby, message);
    }

    private void handleChangeLobbyInfo(User user, Lobby lobby, Message message) throws BaseRuntimeException {
        if (user.equals(lobby.getHost())) {
            message.getContent().forEach((k, v) -> {
                if (k instanceof ContentType) {
                    ContentType key = (ContentType) k;
                    switch (key) {
                        case NAME:
                            lobby.setName(message.getContent(ContentType.NAME));
                            break;
                        case BET_VALUE:
                            lobby.setName(message.getContent(ContentType.BET_VALUE));
                            break;
                    }
                }
            });
            broadcastLobbyInfo(user.getId(), lobby);
        } else throw new InvalidActionException(user.getId());
    }

    @MessageMapping("/play/leave")
    public void processLeaveLobby(@Payload Message message) {
        User user = findUserById(message.getSender());
        Lobby lobby = findLobbyById(message.getContent(ContentType.LOBBY_ID));
        handleLeaveLobby(user, lobby);
    }

    private void handleLeaveLobby(User user, Lobby lobby) {
        Message m = new Message(MessageType.LEAVE_LOBBY, user.getId());
        m.addContent(ContentType.LOBBY_PARTICIPANT, user.getName());
        lobby.removeUser(user);

        if (lobby.getUsers().size() > 0)
            MessagingService.broadcast(lobby.getUsers(), Destination.LEAVE_LOBBY, m);
        else removeLobby(lobby);
    }

    @MessageMapping("/play/start-game")
    public void processStartGame(@Payload Message message) throws BaseRuntimeException {
        User user = findUserById(message.getSender());
        Lobby lobby = findLobbyById(message.getContent(ContentType.LOBBY_ID));

//        boolean canStart = user.equals(lobby.getHost());
//        Message m = new Message(MessageType.START_GAME, message.getSender());
//        m.addContent(ContentType.START, canStart);
//        MessagingService.sendTo(user, Destination.START_GAME, m);

        handleStartGame(user, lobby);
    }

    private void handleStartGame(User user, Lobby lobby) {
        ArrayList<Player> players = new ArrayList<>();
        if (user.equals(lobby.getHost())) {
            lobby.getUsers().forEach(u -> {
                Player p = new Player(u, lobby.getUsers().indexOf(u), lobby.getBetValue());
                players.add(p);
                u.addPlayer(p);
            });

            Room room = new Room(lobby.getId(), lobby.getBetValue(), players);
            UserList.addRoom(room.getId(), lobby.getUsers());
//            entityManager.persist(room);

            roomRepository.save(room);

            Message m = new Message(MessageType.CREATE_ROOM, user.getId());
            m.addContent(ContentType.ROOM_ID, room.getId());
            MessagingService.broadcast(lobby.getUsers(), Destination.CREATE_ROOM, m);
            removeLobby(lobby);
        } else throw new InvalidActionException(user.getId());
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

    private void broadcastLobbyInfo(int userId, Lobby lobby) {
        Message m = new Message(MessageType.JOIN_LOBBY, userId);
        m.addContent(ContentType.LOBBY_ID, lobby.getId())
                .addContent(ContentType.LOBBY_NAME, lobby.getName())
                .addContent(ContentType.LOBBY_PARTICIPANT, lobby.getUsers());
        MessagingService.broadcast(lobby.getUsers(), Destination.FIND_LOBBY, m);
    }

    private void removeLobby(Lobby lobby) {
        Lobby.addDeletedId(lobby.getId());
        lobby.getUsers().forEach(lobby::removeUser);
        lobbyList.remove(lobby);
    }
}
