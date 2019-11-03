package com.bk.olympia.request.socket;

import com.bk.olympia.message.Message;
import com.bk.olympia.message.MessageType;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;

import java.lang.reflect.Type;
import java.util.concurrent.ExecutionException;

import static com.bk.olympia.message.ContentType.*;

public class SocketService {
    private static SocketService instance;
    private static final String host = "localhost";
    private static final int port = 8019;


    private StompSession stompSession;

    public void login(String username, String password, ResponseHandler handler) {
        try {
            stompSession = SocketSendingService.connect(host, port, "/auth");

            System.out.println("Subscribing using session " + stompSession);
            SocketSendingService.subscribe(stompSession, "/user/queue/auth/login", new StompFrameHandler() {
                public Type getPayloadType(StompHeaders stompHeaders) {
                    return byte[].class;
                }

                public void handleFrame(StompHeaders stompHeaders, Object o) {
                    System.out.println("Received message" + new String((byte[]) o));
                    handler.success(o);
                }
            });

            SocketSendingService.subscribe(stompSession, "/user/queue/error", new StompFrameHandler() {
                public Type getPayloadType(StompHeaders stompHeaders) {
                    return byte[].class;
                }

                public void handleFrame(StompHeaders stompHeaders, Object o) {
                    System.out.println("Received error message" + new String((byte[]) o));
                    handler.success(o);
                }
            });

            Message message = new Message(MessageType.LOGIN);
            message.addContent(USERNAME, username)
                    .addContent(PASSWORD, password);

            SocketSendingService.send(stompSession, "/auth/login", message);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            handler.error(e.getMessage());
        }
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

            //Message message = new Message(MessageType.);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SocketService getInstance() {
        if (instance == null) instance = new SocketService();
        return instance;
    }
}
