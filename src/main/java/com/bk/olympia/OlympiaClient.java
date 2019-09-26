package com.bk.olympia;

import com.bk.olympia.message.Message;
import com.bk.olympia.socket.ClientEndPoint;
import com.bk.olympia.socket.SocketService;
import org.springframework.messaging.simp.stomp.StompSession;
import com.bk.olympia.message.MessageType;

import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

public class OlympiaClient {
	public static void main(String[] args) {
		try {
			ClientEndPoint client = SocketService.connect("wss://localhost:8109/login");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

}
