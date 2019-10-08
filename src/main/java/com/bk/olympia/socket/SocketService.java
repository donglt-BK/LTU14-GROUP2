package com.bk.olympia.socket;

import com.bk.olympia.OlympiaClient;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.socket.sockjs.frame.Jackson2SockJsMessageCodec;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SocketService {

    public static StompSession connect(String url) throws ExecutionException, InterruptedException {

        Transport webSocketTransport = new WebSocketTransport(new StandardWebSocketClient());
        List<Transport> transports = Collections.singletonList(webSocketTransport);

        SockJsClient sockJsClient = new SockJsClient(transports);
        sockJsClient.setMessageCodec(new Jackson2SockJsMessageCodec());

        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);

        ListenableFuture<StompSession> f = stompClient.connect(url, new WebSocketHttpHeaders(), new CustomStompSessionHandler(), "localhost", 8109);

        return f.get();

    }

    public static void listen(StompSession stompSession, StompFrameHandler stompFrameHandler) {
        stompSession.subscribe("/queue/login", stompFrameHandler);

    }

    public static void sendHello(StompSession stompSession) {
        String jsonHello = "{ \"name\" : \"Donglt\" }";
        stompSession.send("/app/login", jsonHello.getBytes());
    }

}
