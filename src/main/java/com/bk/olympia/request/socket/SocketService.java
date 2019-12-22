package com.bk.olympia.request.socket;

import com.bk.olympia.message.ErrorMessage;
import com.bk.olympia.message.Message;
import com.bk.olympia.model.UserSession;
import com.bk.olympia.request.handler.ErrorHandler;
import com.bk.olympia.request.handler.ResponseHandler;
import com.bk.olympia.request.handler.StompHandler;
import com.bk.olympia.type.ContentType;
import com.bk.olympia.type.Destination;
import com.bk.olympia.type.MessageType;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSession.Subscription;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.bk.olympia.type.ContentType.*;
import static com.bk.olympia.type.ErrorType.*;

public class SocketService {
    private static SocketService instance;
    private static final String ERROR_URL = "/queue/error";

    private StompSession authSession;
    private StompSession userSession;
    private boolean ready;

    private SocketService() {
        try {
            authSession = SocketSendingService.connect("/auth");
            userSession = SocketSendingService.connect("/user");
            ready = true;
        } catch (ExecutionException | InterruptedException e) {
            System.out.println("Error connect to auth socket");
            ready = false;
        }
    }

    private static Map<String, Subscription[]> subscriptionMap = new HashMap<>();

    private void subscribe(StompSession session, ResponseHandler success, ErrorHandler error, String url) {
        if (subscriptionMap.containsKey(url)) {
            for (Subscription subscription : subscriptionMap.get(url)) {
                subscription.unsubscribe();
            }
        }

        Subscription successSub = SocketSendingService.subscribe(session, url, new StompHandler(success));
        Subscription errorSub = SocketSendingService.subscribe(session, ERROR_URL, new StompHandler(error));
        subscriptionMap.put(url, new Subscription[]{successSub, errorSub});
    }

    public void login(String username, String password, ResponseHandler success, ErrorHandler error) {
        if (!ready) {
            error.handle(new ErrorMessage(CONNECTION_ERROR, -1));
            return;
        }

        subscribe(authSession, success, error, Destination.LOGIN);

        Message message = new Message(MessageType.LOGIN);
        message.addContent(USERNAME, username).addContent(PASSWORD, password);
        SocketSendingService.send(authSession, "/auth/login", message);
    }

    public void signUp(String username, String password, String name, int gender, ResponseHandler success, ErrorHandler error) {
        if (!ready) {
            error.handle(new ErrorMessage(CONNECTION_ERROR, -1));
            return;
        }
        subscribe(authSession, success, error, Destination.SIGN_UP);

        Message message = new Message(MessageType.SIGN_UP);
        message.addContent(USERNAME, username)
                .addContent(NAME, name)
                .addContent(PASSWORD, password)
                .addContent(GENDER, gender);

        SocketSendingService.send(authSession, "/auth/sign_up", message);
    }

    public void getUserInfo(ResponseHandler success, ErrorHandler error) {
        if (!ready) {
            error.handle(new ErrorMessage(CONNECTION_ERROR, -1));
            return;
        }

        subscribe(userSession, success, error, Destination.GET_USER_INFO);

        Message message = new Message(MessageType.GET_INFO);
        SocketSendingService.send(userSession, "/user/get-info", message);
    }

    public void getHistory(ResponseHandler success, ErrorHandler error) {
        if (!ready) {
            error.handle(new ErrorMessage(CONNECTION_ERROR, -1));
            return;
        }
        subscribe(userSession, success, error, Destination.GET_RECENT_HISTORY);

        Message message = new Message(MessageType.GET_RECENT_HISTORY);
        SocketSendingService.send(userSession, "/user/get-recent-history", message);
    }

    public void getTopic(ResponseHandler success, ErrorHandler error) {
        if (!ready) {
            error.handle(new ErrorMessage(CONNECTION_ERROR, -1));
            return;
        }
        subscribe(userSession, success, error, Destination.GET_ALL_TOPICS);

        Message message = new Message(MessageType.GET_ALL_TOPICS);
        SocketSendingService.send(userSession, "/user/get-all-topics", message);
    }

    public void addQuestion(int topic, String question, String[] answer, int pos, ResponseHandler success, ErrorHandler error) {
        if (!ready) {
            error.handle(new ErrorMessage(CONNECTION_ERROR, -1));
            return;
        }
        subscribe(userSession, success, error, Destination.ADD_QUESTION);

        Message message = new Message(MessageType.ADD_QUESTION);
        message.addContent(ContentType.TOPIC_ID, topic);
        message.addContent(QUESTION, question);
        message.addContent(ANSWER, answer);
        message.addContent(CORRECT_ANSWER_POSITION, pos);

        SocketSendingService.send(userSession, "/user/add-question", message);
    }

    public void findGame(ResponseHandler success, ErrorHandler error) {
        if (!ready) {
            error.handle(new ErrorMessage(CONNECTION_ERROR, -1));
            return;
        }

        subscribe(authSession, success, error, Destination.FIND_LOBBY);

        Message message = new Message(MessageType.JOIN_LOBBY);
        message.addContent(BET_VALUE, 2000);
        SocketSendingService.send(authSession, "/play/join", message);
    }

    public void subscribeInvite(ResponseHandler success, ErrorHandler error) {
        if (!ready) {
            error.handle(new ErrorMessage(CONNECTION_ERROR));
            return;
        }

        subscribe(authSession, success, error, Destination.INVITE_PLAYER);
    }

    public void invite(String playerName, ErrorHandler error) {
        if (!ready) {
            error.handle(new ErrorMessage(CONNECTION_ERROR));
            return;
        }

        Message message = new Message(MessageType.JOIN_LOBBY);
        message.addContent(BET_VALUE, 2000)
                .addContent(NAME, playerName);
        SocketSendingService.send(authSession, "/play/invite", message);
    }

    public void replyInvite(Message message, ErrorHandler error) {
        if (!ready) {
            error.handle(new ErrorMessage(CONNECTION_ERROR));
            return;
        }

        SocketSendingService.send(authSession, "/play/invite-rep", message);
    }

    public void readySubscribe(ResponseHandler success, ErrorHandler error) {
        if (!ready) {
            error.handle(new ErrorMessage(CONNECTION_ERROR));
            return;
        }

        subscribe(authSession, success, error, Destination.LOBBY_READY);
    }

    public void sendReady() {
        Message message = new Message(MessageType.READY);
        SocketSendingService.send(userSession, "/play/lobby-ready", message);
    }

    public void leaveLobby(int currentLobbyId) {
        if (!ready) {
            return;
        }
        Message message = new Message(MessageType.LEAVE_LOBBY);
        message.addContent(LOBBY_ID, currentLobbyId);
        SocketSendingService.send(authSession, "/play/leave", message);
    }

    public void subscribeLeaveLobby(ResponseHandler success, ErrorHandler error) {
        if (!ready) {
            return;
        }
        subscribe(authSession, success, error, Destination.LEAVE_LOBBY);
    }

    public void start(ResponseHandler success, ErrorHandler error) {
        if (!ready) {
            error.handle(new ErrorMessage(CONNECTION_ERROR));
            return;
        }

        subscribe(authSession, success, error, Destination.CREATE_ROOM);

        Message message = new Message(MessageType.START_GAME);
        message.addContent(LOBBY_ID, UserSession.getInstance().getCurrentLobbyId());
        SocketSendingService.send(authSession, "/play/start-game", message);
    }

    public void subscribesStart(ResponseHandler success, ErrorHandler error) {
        if (!ready) {
            error.handle(new ErrorMessage(CONNECTION_ERROR));
            return;
        }
        subscribe(authSession, success, error, Destination.CREATE_ROOM);
    }

    public void lobbyChatSubscribe(String lobbyId, ResponseHandler success, ErrorHandler error) {
        if (!ready) {
            error.handle(new ErrorMessage(CONNECTION_ERROR));
            return;
        }

        subscribe(authSession, success, error, Destination.LOBBY_CHAT + lobbyId);
    }

    public void lobbyChat(String text, ErrorHandler error) {
        if (!ready) {
            error.handle(new ErrorMessage(CONNECTION_ERROR));
            return;
        }

        Message message = new Message(MessageType.LOGIN);
        message.addContent(ContentType.CHAT, text);
        SocketSendingService.send(authSession, "/topic/private/lobby/" + UserSession.getInstance().getCurrentLobbyId(), message);
    }

    public void roomChatSubscribe(ResponseHandler success, ErrorHandler error) {
        if (!ready) {
            error.handle(new ErrorMessage(CONNECTION_ERROR));
            return;
        }

        System.out.println(Destination.ROOM_CHAT + UserSession.getInstance().getRoomId());
        subscribe(authSession, success, error, Destination.ROOM_CHAT + UserSession.getInstance().getRoomId());
    }

    public void roomChat(String text, ErrorHandler error) {
        if (!ready) {
            error.handle(new ErrorMessage(CONNECTION_ERROR));
            return;
        }

        Message message = new Message(MessageType.LOGIN);
        message.addContent(ContentType.CHAT, text);
        SocketSendingService.send(authSession, "/topic/private/room/" + UserSession.getInstance().getRoomId(), message);
    }

    public static SocketService getInstance() {
        if (instance == null)
            instance = new SocketService();
        return instance;
    }

    public void getQuestion(ErrorHandler error) {
        if (!ready) {
            error.handle(new ErrorMessage(CONNECTION_ERROR));
            return;
        }

        Message message = new Message(MessageType.GET_QUESTION);
        SocketSendingService.send(authSession, "/play/get-question", message);
    }

    public void subscribeQuestion(ResponseHandler success, ErrorHandler error) {
        if (!ready) {
            error.handle(new ErrorMessage(CONNECTION_ERROR));
            return;
        }
        subscribe(authSession, success, error, Destination.GET_QUESTION);
    }

    public void submit(int questionId, int[] placement, ErrorHandler error) {
        if (!ready) {
            error.handle(new ErrorMessage(CONNECTION_ERROR));
            return;
        }

        Message message = new Message(MessageType.SUBMIT_ANSWER);
        message.addContent(QUESTION_ID, questionId)
                .addContent(MONEY_PLACED, placement);
        SocketSendingService.send(authSession, "/play/submit-answer", message);
    }

    public void receiveAnswer(ResponseHandler success, ErrorHandler error) {
        if (!ready) {
            error.handle(new ErrorMessage(CONNECTION_ERROR));
            return;
        }
        subscribe(authSession, success, error, Destination.SUBMIT_ANSWER);
    }

    public void gameOver(ResponseHandler success, ErrorHandler error) {
        if (!ready) {
            error.handle(new ErrorMessage(CONNECTION_ERROR));
            return;
        }
        subscribe(authSession, success, error, Destination.GAME_OVER);
        if (UserSession.getInstance().isAlpha()) {
            Message message = new Message(MessageType.GAME_OVER);
            SocketSendingService.send(authSession, "/play/game-over", message);
        }
    }

    public void subscribeSurrender(ResponseHandler success, ErrorHandler error) {
        if (!ready) {
            error.handle(new ErrorMessage(CONNECTION_ERROR));
            return;
        }
        subscribe(authSession, success, error, Destination.SURRENDER);
    }

    public void surrender(ErrorHandler error) {
        if (!ready) {
            error.handle(new ErrorMessage(CONNECTION_ERROR));
            return;
        }
        Message message = new Message(MessageType.SURRENDER);
        SocketSendingService.send(authSession, "/play/surrender", message);
    }

}
