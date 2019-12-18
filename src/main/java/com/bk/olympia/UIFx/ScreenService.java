package com.bk.olympia.UIFx;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class ScreenService {
    @FXML
    public void changeScreen(Event event, String screenName) {
        Stage currentScreen = (Stage)((Node) event.getSource()).getScene().getWindow();
        Parent screenToChange = null;
        try {
            screenToChange = FXMLLoader.load(getClass().getClassLoader().getResource(screenName + ".fxml"));
            System.out.println("load: " + getClass().getClassLoader().getResource(screenName + ".fxml"));
        } catch (IOException e) {
            System.out.println("ERRROR: " + e.getMessage());
        }
        try {
            changeScreen(currentScreen, screenToChange);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeScreen(Stage currentScreen, String screenName) {
        Parent screenToChange = null;
        try {
            screenToChange = FXMLLoader.load(getClass().getClassLoader().getResource(screenName + ".fxml"));
            System.out.println("load: " + getClass().getClassLoader().getResource(screenName + ".fxml"));
        } catch (IOException e) {
            System.out.println("ERRROR: " + e.getMessage());
        }
        try {
            changeScreen(currentScreen, screenToChange);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void changeScreen(Stage stage, Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void showWarning(String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning alert");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
    public void showError(String headerText, String contentText){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error alert");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    public void addParticipant() {

    };
}
