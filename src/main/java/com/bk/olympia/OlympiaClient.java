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
    public void start(Stage primaryStage) throws Exception {
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));
            primaryStage.setTitle("Hello World");
            primaryStage.setScene(new Scene(root, 600, 400));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {

        launch(args);
    }
}
