package com.bk.olympia.request.handler;

import com.bk.olympia.message.ErrorMessage;
import com.bk.olympia.message.Message;
import com.google.gson.Gson;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;

public class StompHandler implements StompFrameHandler {
    private Gson gson = new Gson();
    private ResponseHandler handler;
    private ErrorHandler errorHandler;
    private boolean isErrorHandler;

    public StompHandler(ResponseHandler handler) {
        this.handler = handler;
        isErrorHandler = false;
    }

    public StompHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        isErrorHandler = true;
    }

    public Type getPayloadType(StompHeaders stompHeaders) {
        return byte[].class;
    }

    public void handleFrame(StompHeaders stompHeaders, Object o) {
        System.out.println("handle " + new String((byte[]) o));
        if (isErrorHandler) {
            errorHandler.handle(gson.fromJson(new String((byte[]) o), ErrorMessage.class));
        } else {
            handler.handle(gson.fromJson(new String((byte[]) o), Message.class));
        }
    }
}
