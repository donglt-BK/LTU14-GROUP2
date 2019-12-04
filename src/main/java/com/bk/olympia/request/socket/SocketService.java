package com.bk.olympia.request.socket;

import com.bk.olympia.message.ErrorMessage;
import com.bk.olympia.message.Message;
import com.bk.olympia.request.handler.ErrorHandler;
import com.bk.olympia.request.handler.ResponseHandler;
import com.bk.olympia.request.handler.StompHandler;
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
            authSession = SocketSendingService.connect("localhost", 8109, "/auth");
            userSession = SocketSendingService.connect("localhost", 8109, "/user");
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

    //TODO check session
    public void invite(String playerID, ResponseHandler success, ErrorHandler error) {
        if (!ready) {
            error.handle(new ErrorMessage(CONNECTION_ERROR));
            return;
        }

        subscribe(userSession, success, error, "/queue/play/invite");

        Message message = new Message(MessageType.JOIN_LOBBY);
        message.addContent(BET_VALUE, 2000)
                .addContent(NAME, playerID);
        SocketSendingService.send(authSession, "/play/invite", message);
    }

    public void ready(String playerID, ResponseHandler success, ErrorHandler error) {
        if (!ready) {
            error.handle(new ErrorMessage(CONNECTION_ERROR));
            return;
        }

        subscribe(authSession, success, error, Destination.READY);

        Message message = new Message(MessageType.READY);
        SocketSendingService.send(authSession, "/play/ready", message);
    }

    public void cancelLobby() {

    }
    public static SocketService getInstance() {
        if (instance == null)
            instance = new SocketService();
        return instance;
    }
}
