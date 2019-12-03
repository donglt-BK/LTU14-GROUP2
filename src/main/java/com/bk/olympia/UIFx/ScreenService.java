package com.bk.olympia.UIFx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ScreenService {
    @FXML
    public void onChangeScreen(ActionEvent event, String screenName) throws IOException {
        Stage currentScreen = (Stage)((Node) event.getSource()).getScene().getWindow();
        Parent screenToChange = FXMLLoader.load(getClass().getClassLoader().getResource(screenName + ".fxml"));
        changeScreen(currentScreen, screenToChange);
    }

    private static void changeScreen(Stage stage, Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
