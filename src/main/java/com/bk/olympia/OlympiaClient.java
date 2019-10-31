package com.bk.olympia;

import com.bk.olympia.UI.JFrameUI;
import com.bk.olympia.UI.ScreenService;
import com.bk.olympia.exception.ScreenNotFoundException;
import com.bk.olympia.message.ContentType;
import com.bk.olympia.message.Message;
import com.bk.olympia.message.MessageType;
import com.bk.olympia.socket.SocketService;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;

import java.lang.reflect.Type;

import static com.bk.olympia.config.Constant.*;

public class OlympiaClient {

/*
	public static void main(String[] args) {
		StompSession stompSession = SocketService.connect("localhost", 8109, "/login");

		System.out.println("Subscribing using session " + stompSession);
		SocketService.subscribe(stompSession, "/user/queue/login", new StompFrameHandler() {
			public Type getPayloadType(StompHeaders stompHeaders) {
				return byte[].class;
			}

			public void handleFrame(StompHeaders stompHeaders, Object o) {
				System.out.println("Received message" + new String((byte[]) o));
			}
		});

		Message m = new Message(MessageType.LOGIN);
		m.addContent(ContentType.USERNAME, "donglt")
		.addContent(ContentType.PASSWORD, "donglt");

		SocketService.send(stompSession, "/app/login", m);
	}
*/

	public static void main(String[] args) {

		JFrameUI ui = JFrameUI.getInstance().config(APP_NAME, WINDOW_WIDTH, WINDOW_HEIGHT);
		ui.addScreen(ScreenService.getScreens());

		try {
			ui.showScreen(LOGIN_SCREEN);
		} catch (ScreenNotFoundException e) {
			e.printStackTrace();
		}
	}

}
