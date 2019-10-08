package com.bk.olympia;

import com.bk.olympia.socket.SocketService;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.socket.sockjs.frame.Jackson2SockJsMessageCodec;

import java.lang.reflect.Type;

public class OlympiaClient {

	public static void main(String[] args) throws Exception {
		String url = "ws://{host}:{port}/login";

		StompSession stompSession = SocketService.connect(url);

		System.out.println("Subscribing to greeting topic using session " + stompSession);
		SocketService.listen(stompSession, new StompFrameHandler() {
			public Type getPayloadType(StompHeaders stompHeaders) {
				return byte[].class;
			}

			public void handleFrame(StompHeaders stompHeaders, Object o) {
				System.out.println("Received greeting " + new String((byte[]) o));
			}
		});
		SocketService.sendHello(stompSession);
	}

}
