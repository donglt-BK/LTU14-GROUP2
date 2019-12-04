package com.bk.olympia.request.handler;

import com.bk.olympia.message.ErrorMessage;
import com.bk.olympia.message.Message;
import com.bk.olympia.request.socket.ResponseHandler;
import com.google.gson.Gson;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;

public class StompErrorHandler implements StompFrameHandler {
    private Gson gson;
    private ResponseHandler handler;

    public StompErrorHandler(ResponseHandler handler) {
        this.handler = handler;
        gson = new Gson();
    }

    public Type getPayloadType(StompHeaders stompHeaders) {
        return byte[].class;
    }

    public void handleFrame(StompHeaders stompHeaders, Object o) {
        handler.handle(gson.fromJson(new String((byte[]) o), ErrorMessage.class));
    }
}
