package com.bk.olympia.request.handler;

import com.bk.olympia.message.ErrorMessage;
import com.bk.olympia.message.Message;
import com.bk.olympia.request.socket.ResponseHandler;
import com.google.gson.Gson;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;

public class CustomStompFrameHandler implements StompFrameHandler {
    private Gson gson;
    private ResponseHandler handler;
    private boolean isErrorHandler;

    public CustomStompFrameHandler(ResponseHandler handler, boolean isErrorHandler) {
        this.handler = handler;
        this.isErrorHandler = isErrorHandler;
        gson = new Gson();
    }

    public Type getPayloadType(StompHeaders stompHeaders) {
        return byte[].class;
    }

    public void handleFrame(StompHeaders stompHeaders, Object o) {
        if (isErrorHandler) {
            ErrorMessage message = gson.fromJson(new String((byte[]) o), ErrorMessage.class);
            handler.error(message);
        } else {
            Message message = gson.fromJson(new String((byte[]) o), Message.class);
            handler.success(message);
        }
    }
}
