package com.bk.olympia.UIFx;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;
import java.util.Optional;

import static com.bk.olympia.config.Constant.LOGIN_SCREEN;

public class HomeController extends ScreenService{
    @FXML
    TextField playerId;

    public void findPlayer(){
        System.out.println("Finding another player...");
        Platform.runLater(()->{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Find random player");
            alert.setTitle("Finding another player, please wait...");
                                
        });
    }
    public void invitePlayer(){
        System.out.println("Waiting for response...");
    }
    public void signOut(ActionEvent event){
        Platform.runLater(()->{
            try{
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Sign Out");
                alert.setHeaderText("Signing out of the game");
                alert.setContentText("Are you sure you want to sign out?");
                alert.getButtonTypes().clear();

                ButtonType signOut = new ButtonType("Sign out");
                ButtonType cancel = new ButtonType("Cancel");
                alert.getButtonTypes().addAll(signOut, cancel);

                Optional<ButtonType> option = alert.showAndWait();

                if(option.get() == signOut){
                    changeScreen(event, LOGIN_SCREEN);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        });
    }
}
