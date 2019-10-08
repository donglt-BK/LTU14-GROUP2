package com.bk.olympia.socket;

import org.springframework.messaging.simp.stomp.*;

import java.lang.reflect.Type;

public class CustomStompSessionHandler extends StompSessionHandlerAdapter {
    public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
        System.out.println("Now connected");
    }
}
