package com.bk.olympia;

import com.bk.olympia.UI.JFrameUI;
import com.bk.olympia.UI.ScreenService;
import com.bk.olympia.exception.ScreenNotFoundException;

import static com.bk.olympia.config.Constant.*;

public class OlympiaClient {

	/*public static void main(String[] args) {
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
	}*/

	public static void main(String[] args) {

		JFrameUI ui = JFrameUI.getInstance().config(APP_NAME, WINDOW_WIDTH, WINDOW_HEIGHT);
		ui.addScreen(ScreenService.generateScreens(WINDOW_WIDTH, WINDOW_HEIGHT));

		try {
			ui.showScreen(LOGIN_SCREEN);
		} catch (ScreenNotFoundException e) {
			e.printStackTrace();
		}
	}

}
