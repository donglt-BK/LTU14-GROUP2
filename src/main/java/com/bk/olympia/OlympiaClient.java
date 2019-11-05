package com.bk.olympia;

import com.bk.olympia.UI.JFrameUI;
import com.bk.olympia.UI.ScreenService;
import com.bk.olympia.exception.ScreenNotFoundException;
import com.bk.olympia.message.ErrorMessage;
import com.bk.olympia.request.socket.ResponseHandler;
import com.bk.olympia.request.socket.SocketService;
import com.bk.olympia.type.ErrorType;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;

import static com.bk.olympia.config.Constant.*;

public class OlympiaClient {

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
