package com.bk.olympia;

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
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class OlympiaClient {
	private final static WebSocketHttpHeaders headers = new WebSocketHttpHeaders();

	public ListenableFuture<StompSession> connect() {

		Transport webSocketTransport = new WebSocketTransport(new StandardWebSocketClient());
		List<Transport> transports = Collections.singletonList(webSocketTransport);

		SockJsClient sockJsClient = new SockJsClient(transports);
		sockJsClient.setMessageCodec(new Jackson2SockJsMessageCodec());

		WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);

		String url = "ws://{host}:{port}/login";
		return stompClient.connect(url, headers, new MyHandler(), "localhost", 8109);
	}

	public void subscribeGreetings(StompSession stompSession) throws ExecutionException, InterruptedException {
		stompSession.subscribe("/queue/login", new StompFrameHandler() {

			public Type getPayloadType(StompHeaders stompHeaders) {
				return byte[].class;
			}

			public void handleFrame(StompHeaders stompHeaders, Object o) {
				System.out.println("Received greeting " + new String((byte[]) o));
			}
		});
	}

	public void sendHello(StompSession stompSession) {
		String jsonHello = "{ \"name\" : \"Donglt\" }";
		stompSession.send("/app/login", jsonHello.getBytes());
	}

	private class MyHandler extends StompSessionHandlerAdapter {
		public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
			System.out.println("Now connected");
		}
	}

	public static void main(String[] args) throws Exception {
		OlympiaClient helloClient = new OlympiaClient();

		ListenableFuture<StompSession> f = helloClient.connect();
		StompSession stompSession = f.get();

		System.out.println("Subscribing to greeting topic using session " + stompSession);
		helloClient.subscribeGreetings(stompSession);

		System.out.println("Sending hello message" + stompSession);
		helloClient.sendHello(stompSession);
		Thread.sleep(60000);
	}

}
