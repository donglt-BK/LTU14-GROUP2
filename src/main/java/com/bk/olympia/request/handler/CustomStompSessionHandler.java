package com.bk.olympia.request.handler;

import org.springframework.messaging.simp.stomp.*;

public class CustomStompSessionHandler extends StompSessionHandlerAdapter {
    public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
        System.out.println("Now connected");
    }
}
