package com.bk.olympia.request.socket;

import com.bk.olympia.message.ErrorMessage;
import com.bk.olympia.message.Message;
import com.bk.olympia.request.handler.CustomStompFrameHandler;
import com.bk.olympia.request.handler.StompErrorHandler;
import com.bk.olympia.request.handler.StompSuccessHandler;
import com.bk.olympia.type.MessageType;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.concurrent.ExecutionException;

import static com.bk.olympia.type.ContentType.*;
import static com.bk.olympia.type.ErrorType.*;

public class SocketService {
    private static SocketService instance;


    private StompSession authSession;
    private boolean ready;

    private SocketService() {
        try {
            authSession = SocketSendingService.connect("localhost", 8109, "/auth");
            ready = true;
        } catch (ExecutionException | InterruptedException e) {
            System.out.println("Error connect to auth socket");
            ready = false;
        }
    }

    public void login(String username, String password, ResponseHandler success, ResponseHandler error) {
        if (!ready) {
            error.handle(new ErrorMessage(CONNECTION_ERROR, -1));
            return;
        }

        SocketSendingService.subscribe(authSession, "/queue/auth/login", new StompSuccessHandler(success));
        SocketSendingService.subscribe(authSession, "/queue/error", new StompErrorHandler(error));

        Message message = new Message(MessageType.LOGIN);
        message.addContent(USERNAME, username).addContent(PASSWORD, password);
        SocketSendingService.send(authSession, "/auth/login", message);
    }

    @Deprecated
    public void login(String username, String password, OldResponseHandler handler) {
        if (!ready) {
            handler.error(new ErrorMessage(CONNECTION_ERROR, -1));
            return;
        }

        SocketSendingService.subscribe(authSession, "/queue/auth/login", new CustomStompFrameHandler(handler, false));
        SocketSendingService.subscribe(authSession, "/queue/error", new CustomStompFrameHandler(handler, true));

        Message message = new Message(MessageType.LOGIN);
        message.addContent(USERNAME, username).addContent(PASSWORD, password);
        SocketSendingService.send(authSession, "/auth/login", message);
    }

    public void signUp(String username, String password, String name, int gender, ResponseHandler success, ResponseHandler error) {
        if (!ready) {
            error.handle(new ErrorMessage(CONNECTION_ERROR, -1));
            return;
        }
        SocketSendingService.subscribe(authSession, "/queue/auth/sign_up", new StompSuccessHandler(success));
        SocketSendingService.subscribe(authSession, "/queue/error", new StompErrorHandler(error));

        Message message = new Message(MessageType.SIGN_UP);
        message.addContent(USERNAME, username)
                .addContent(PASSWORD, password)
                .addContent(GENDER, gender);

        SocketSendingService.send(authSession, "/auth/sign_up", message);
    }

    @Deprecated
    public void signUp(String username, String password, String name, int gender, OldResponseHandler handler) {
        if (!ready) {
            handler.error(new ErrorMessage(CONNECTION_ERROR, -1));
            return;
        }
        SocketSendingService.subscribe(authSession, "/queue/auth/sign_up", new CustomStompFrameHandler(handler, false));
        SocketSendingService.subscribe(authSession, "/queue/error", new CustomStompFrameHandler(handler, true));

        Message message = new Message(MessageType.SIGN_UP);
        message.addContent(USERNAME, username)
                .addContent(NAME, name)
                .addContent(PASSWORD, password)
                .addContent(GENDER, gender);

        SocketSendingService.send(authSession, "/auth/sign_up", message);
    }

    public static SocketService getInstance() {
        if (instance == null)
            instance = new SocketService();
        return instance;
    }
}
