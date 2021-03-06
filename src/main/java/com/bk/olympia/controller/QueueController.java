package com.bk.olympia.controller;

import com.bk.olympia.base.BaseController;
import com.bk.olympia.constant.ContentType;
import com.bk.olympia.constant.Destination;
import com.bk.olympia.constant.MessageType;
import com.bk.olympia.event.DisconnectUserFromLobbyEvent;
import com.bk.olympia.exception.InsufficientBalanceException;
import com.bk.olympia.exception.TargetInsufficientBalanceException;
import com.bk.olympia.exception.UnauthorizedActionException;
import com.bk.olympia.exception.UserNameNotFoundException;
import com.bk.olympia.model.Lobby;
import com.bk.olympia.model.entity.Player;
import com.bk.olympia.model.entity.Room;
import com.bk.olympia.model.entity.User;
import com.bk.olympia.model.message.Message;
import com.bk.olympia.model.message.MessageAccept;
import com.google.common.util.concurrent.Striped;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.stream.Collectors;

@Controller
public class QueueController extends BaseController implements ApplicationListener<ApplicationEvent> {
    private final Striped<ReadWriteLock> lockStriped = Striped.lazyWeakReadWriteLock(32);
    private static Map<Lobby, Integer> lobbyList = new TreeMap<>();

    @Autowired
    private ChatController chatController;

    @Override
    protected void init() {
        this.logger = LoggerFactory.getLogger(QueueController.class);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof DisconnectUserFromLobbyEvent) {
            handleLeaveLobby(((DisconnectUserFromLobbyEvent) event).getUser(), findLobbyById(((DisconnectUserFromLobbyEvent) event).getLobbyId()));
        }
    }

    @MessageMapping("/play/join")
    public void processFindLobby(@Payload Message message) {
        User user = findUserById(message.getSender());
        int betValue = message.getContent(ContentType.BET_VALUE);

        handleFindLobby(user, betValue);
    }

    private void handleFindLobby(User user, int betValue) {
        if (betValue > user.getBalance())
            throw new InsufficientBalanceException(user.getId());
        if (user.getLobbyId() > 0)
            throw new UnauthorizedActionException(user.getId());

        sendTo(user, Destination.FIND_LOBBY, new MessageAccept(MessageType.JOIN_LOBBY, user.getId()));
        Lobby lobby = findLobbyByBetValue(betValue);
        lobby.addUser(user);

        chatController.lobbyUserMap.put(user.getId(), lobby.getId());
        save(user);
        lobbyList.put(lobby, lobby.getId());
        broadcastLobbyInfo(user.getId(), lobby);
    }

    @MessageMapping("/play/invite")
    public void processInvite(@Payload Message message) {
        User user = findUserById(message.getSender());
        User recipient = findUserByName(message.getContent(ContentType.NAME));
        int betValue = message.getContent(ContentType.BET_VALUE);

        handleInvite(user, recipient, betValue, message);
    }

    private void handleInvite(User user, User recipient, int betValue, Message message) {
        if (betValue > user.getBalance())
            throw new InsufficientBalanceException(user.getId());
        if (recipient == null)
            throw new UserNameNotFoundException(user.getId());
        if (recipient.getBalance() >= betValue) {
            ReadWriteLock lock = lockStriped.get(recipient);
            lock.readLock().lock();
            try {
                message.addContent(ContentType.USERNAME, user.getName());
                sendTo(recipient, Destination.INVITE_PLAYER, message);
            } finally {
                lock.readLock().unlock();
            }
        } else throw new TargetInsufficientBalanceException(user.getId());
    }

    @MessageMapping("/play/invite-rep")
    public void processReplyInvite(@Payload Message message) {
        User user = findUserById(message.getSender());
        User recipient = findUserByName(message.getContent(ContentType.NAME));

        handleReplyInvite(user, recipient, message);
    }

    private void handleReplyInvite(User user, User recipient, Message message) {
        sendTo(user, Destination.INVITE_PLAYER, message);
        if (message.getContent(ContentType.REPLY)) {
            Double d = message.getContent(ContentType.BET_VALUE);
            Lobby lobby = new Lobby(d.intValue());
            lobby.addUser(recipient)
                    .addUser(user);
            broadcastLobbyInfo(user.getId(), lobby);
        }
    }

    @MessageMapping("/play/change-info")
    public void processChangeLobbyInfo(@Payload Message message) {
        User user = findUserById(message.getSender());
        Lobby lobby = findLobbyById(message.getContent(ContentType.LOBBY_ID));

        handleChangeLobbyInfo(user, lobby, message);
    }

    private void handleChangeLobbyInfo(User user, Lobby lobby, Message message) {
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
        } else throw new UnauthorizedActionException(user.getId());
    }

    @MessageMapping("/play/leave")
    public void processLeaveLobby(@Payload Message message) {
        User user = findUserById(message.getSender());
        Lobby lobby = findLobbyById(message.getContent(ContentType.LOBBY_ID));
        handleLeaveLobby(user, lobby);
    }

    private void handleLeaveLobby(User user, Lobby lobby) {
        if (lobby == null) return;
        Message m = new Message(MessageType.LEAVE_LOBBY, user.getId());
        m.addContent(ContentType.LOBBY_PARTICIPANT, user.getName());
        lobby.removeUser(user);
        chatController.lobbyUserMap.put(user.getId(), -1);
        user.setLobbyId(-1);
        userRepository.save(user);

        if (lobby.getUsers().size() > 0)
            broadcast(lobby.getUsers(), Destination.LEAVE_LOBBY, m);
        else removeLobby(lobby);
    }

    @MessageMapping("/play/lobby-ready")
    public void processReady(@Payload Message message) {
        User user = findUserById(message.getSender());
        Lobby lobby = findLobbyById(user.getLobbyId());
        handleReady(user, lobby);
    }

    private void handleReady(User user, Lobby lobby) {
        if (lobby.getHost().equals(user)) {
            throw new UnauthorizedActionException(user.getId());
        }
        int pos = lobby.getUsers().indexOf(user);

        boolean isReady = false;
        if (lobby.getReadyList().get(pos)) {
            lobby.removePlayerReady(pos);
        } else {
            isReady = true;
            lobby.addPlayerReady(pos);
        }
        Message m = new Message(MessageType.LOBBY_READY, user.getId());
        m.addContent(ContentType.READY, isReady);
        sendTo(lobby.getHost(), Destination.LOBBY_READY, m);
    }


    @MessageMapping("/play/start-game")
    public void processStartGame(@Payload Message message) {
        User user = findUserById(message.getSender());
        Lobby lobby = findLobbyById(message.getContent(ContentType.LOBBY_ID));
        handleStartGame(user, lobby);
    }

    private void handleStartGame(User user, Lobby lobby) {
        ArrayList<Player> players = new ArrayList<>();
        if (user.equals(lobby.getHost())) {
            lobby.getUsers().forEach(u -> {
                Player p = new Player(u, lobby.getUsers().indexOf(u), lobby.getBetValue());
                u.addPlayer(p);
                players.add(p);
                save(p);
            });

            Room room = new Room(lobby.getBetValue(), players);
            room.setLobbyId(lobby.getId());
            roomRepository.save(room);
            room.getPlayerList().forEach(p -> {
                p.setRoom(room);
                save(p);
            });

            Message m = new Message(MessageType.CREATE_ROOM, user.getId());
            m.addContent(ContentType.ROOM_ID, room.getId());
            broadcast(lobby.getUsers(), Destination.CREATE_ROOM, m);
            removeLobby(lobby);
        } else throw new UnauthorizedActionException(user.getId());
    }

    private Lobby findLobbyByBetValue(int betValue) {
        ReadWriteLock lock = lockStriped.get(betValue);
        lock.readLock().lock();
        try {
            return lobbyList.keySet().stream()
                    .filter(lobby -> lobby.getUsers().size() < 2)
                    .filter(lobby -> lobby.getBetValue() == betValue)
                    .findAny()
                    .orElse(new Lobby(betValue));
        } finally {
            lock.readLock().unlock();
        }
    }

    public Lobby findLobbyById(int id) {
        return lobbyList.keySet().stream()
                .filter(integer -> integer.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private void broadcastLobbyInfo(int userId, Lobby lobby) {
        Message m = new Message(MessageType.JOIN_LOBBY, userId);
        m.addContent(ContentType.LOBBY_ID, lobby.getId())
                .addContent(ContentType.LOBBY_NAME, lobby.getName())
                .addContent(ContentType.LOBBY_PARTICIPANT, lobby.getUsers().stream().map(User::getName).collect(Collectors.toList()));
        broadcast(lobby.getUsers(), Destination.FIND_LOBBY, m);
    }

    private void removeLobby(Lobby lobby) {
        Lobby.addDeletedId(lobby.getId());
        lobby.getUsers().forEach(user -> user.setLobbyId(-1));
        lobbyList.remove(lobby);
    }
}
