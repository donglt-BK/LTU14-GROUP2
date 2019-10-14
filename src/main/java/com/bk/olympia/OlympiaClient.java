package com.bk.olympia;

import com.bk.olympia.message.Message;
import com.bk.olympia.message.MessageType;
import com.bk.olympia.socket.SocketService;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;

import java.lang.reflect.Type;

public class OlympiaClient {

	public static void main(String[] args) throws Exception {
		StompSession stompSession = SocketService.connect("localhost", 8109, "/login");

		System.out.println("Subscribing using session " + stompSession);
		SocketService.subscribe(stompSession, "/queue/login", new StompFrameHandler() {
			public Type getPayloadType(StompHeaders stompHeaders) {
				return byte[].class;
			}

			public void handleFrame(StompHeaders stompHeaders, Object o) {
				System.out.println("Received message" + new String((byte[]) o));
			}
		});

		Message m = new Message(MessageType.LOGIN);
		m.addContent("username", "donglt")
		.addContent("password", "donglt");

		SocketService.send(stompSession, "/app/login", m);
	}

}
