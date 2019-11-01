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

    private StompSession stompSession;

    public void login(String username, String password, ResponseHandler handler) {
        try {
            stompSession = SocketSendingService.connect("localhost", 8109, "/login");

            System.out.println("Subscribing using session " + stompSession);
            SocketSendingService.subscribe(stompSession, "/queue/login", new StompFrameHandler() {
                public Type getPayloadType(StompHeaders stompHeaders) {
                    return byte[].class;
                }

                public void handleFrame(StompHeaders stompHeaders, Object o) {
                    System.out.println("Received message" + new String((byte[]) o));
                    handler.success(o);
                }
            });

            Message message = new Message(MessageType.LOGIN);
            message.addContent(USERNAME, username).addContent(PASSWORD, password);

            SocketSendingService.send(stompSession, "/app/login", message);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            handler.error(e.getMessage());
        }
    }

    public static SocketService getInstance() {
        if (instance == null) instance = new SocketService();
        return instance;
    }
}
