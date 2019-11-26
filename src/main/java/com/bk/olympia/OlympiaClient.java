package com.bk.olympia;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class OlympiaClient extends Application {

//	public static void main(String[] args) {
//		JFrameUI ui = JFrameUI.getInstance().config(APP_NAME, WINDOW_WIDTH, WINDOW_HEIGHT);
//		ui.addScreen(ScreenService.getScreens());
//
//		try {
//			ui.showScreen(LOGIN_SCREEN);
//		} catch (ScreenNotFoundException e) {
//			e.printStackTrace();
//		}
//	}

	@Override
	public void start(Stage primaryStage) throws Exception{
		Parent root = FXMLLoader.load(getClass().getResource("UIFx/sample.fxml"));
		primaryStage.setTitle("Hello World");
		primaryStage.setScene(new Scene(root, 300, 275));
		primaryStage.show();
	}


	public static void main(String[] args) {
		launch(args);
	}
}
