package com.bk.olympia.request.socket;

import com.bk.olympia.message.ErrorMessage;
import com.bk.olympia.message.Message;
import com.bk.olympia.type.MessageType;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;

import java.lang.reflect.Type;
import java.util.concurrent.ExecutionException;

import static com.bk.olympia.type.ContentType.*;
import static com.bk.olympia.type.ErrorType.*;

public class SocketService {
    private static SocketService instance;
    private static final String host = "localhost";
    private static final int port = 8109;


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

    public void login(String username, String password, ResponseHandler handler) {
        if (!ready) {
            handler.error(new ErrorMessage(CONNECTION_ERROR, -1));
            return;
        }

        SocketSendingService.subscribe(authSession, "/queue/auth/login", new StompFrameHandler() {
            public Type getPayloadType(StompHeaders stompHeaders) {
                return byte[].class;
            }

            public void handleFrame(StompHeaders stompHeaders, Object o) {
                handler.success((Message) o);
            }
        });
        SocketSendingService.subscribe(authSession, "/queue/error", new StompFrameHandler() {
            public Type getPayloadType(StompHeaders stompHeaders) {
                return byte[].class;
            }

            public void handleFrame(StompHeaders stompHeaders, Object o) {
                System.out.println("error " + new String((byte[]) o));
                try {
                    handler.error((ErrorMessage) o);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
            }
        });

        Message message = new Message(MessageType.LOGIN);
        message.addContent(USERNAME, username).addContent(PASSWORD, password);
        SocketSendingService.send(authSession, "/auth/login", message);
    }

    public void signUp(String username, String password, String name, int gender, ResponseHandler handler) {
        try {
            stompSession = SocketSendingService.connect(host, port, "/auth");
            System.out.println(stompSession);
            SocketSendingService.subscribe(stompSession, "/user/queue/auth/sign_up", new StompFrameHandler() {
                @Override
                public Type getPayloadType(StompHeaders stompHeaders) {
                    return byte[].class;
                }

                @Override
                public void handleFrame(StompHeaders stompHeaders, Object o) {
                    handler.success(o);
                }
            });

            Message message = new Message(MessageType.SIGNUP);
            message.addContent(USERNAME, username)
                    .addContent(PASSWORD, password)
                    .addContent(GENDER, gender);

            SocketSendingService.send(stompSession, "/auth/sign_up", message);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            handler.error(e.getMessage());
        }
    }

    public static SocketService getInstance() {
        if (instance == null)
            instance = new SocketService();
        return instance;
    }
}
