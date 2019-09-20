package com.bk.olympia.socket;

import com.bk.olympia.message.Message;
import com.bk.olympia.message.MessageType;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

import java.lang.reflect.Type;

public class CustomStompSessionHandler implements StompSessionHandler {

    public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
        System.out.println("check");
        stompSession.send("app/login", new Message(MessageType.LOGIN, 1));
    }
    public void handleFrame(StompHeaders headers, Object payload) {
        Message msg = (Message) payload;
        System.out.println(msg.toString());
    }

    public void handleException(StompSession stompSession, StompCommand stompCommand, StompHeaders stompHeaders, byte[] bytes, Throwable throwable) {

    }

    public void handleTransportError(StompSession stompSession, Throwable throwable) {

    }

    public Type getPayloadType(StompHeaders stompHeaders) {
        return null;
    }
}
