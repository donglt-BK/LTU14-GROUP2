package com.bk.olympia.socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.socket.sockjs.frame.Jackson2SockJsMessageCodec;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SocketService {
    private static final String templateUrl = "ws://{host}:{port}";

    public static StompSession connect(String host, int port, String path) throws ExecutionException, InterruptedException {

        Transport webSocketTransport = new WebSocketTransport(new StandardWebSocketClient());
        List<Transport> transports = Collections.singletonList(webSocketTransport);

        SockJsClient sockJsClient = new SockJsClient(transports);
        sockJsClient.setMessageCodec(new Jackson2SockJsMessageCodec());

        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);

        ListenableFuture<StompSession> f = stompClient.connect(templateUrl + path, new WebSocketHttpHeaders(), new CustomStompSessionHandler(), host, port);

        return f.get();

    }

    public static void subscribe(StompSession stompSession, String url, StompFrameHandler stompFrameHandler) {
        stompSession.subscribe(url, stompFrameHandler);

    }

    public static void send(StompSession stompSession, String url, Object object) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            stompSession.send(url, objectMapper.writeValueAsString(object).getBytes());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
