package com.bk.olympia;

import com.bk.olympia.message.Message;
import com.bk.olympia.socket.SocketService;
import org.springframework.messaging.simp.stomp.StompSession;
import com.bk.olympia.message.MessageType;

import java.util.concurrent.ExecutionException;

public class OlympiaClient {
	public static void main(String[] args) {
		try {
			StompSession session = SocketService.connect("wss://localhost:8109/play");
			//session.send("app/login", new Message(MessageType.LOGIN, 1));
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
