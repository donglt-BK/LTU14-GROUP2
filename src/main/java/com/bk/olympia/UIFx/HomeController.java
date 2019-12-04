package com.bk.olympia.UIFx;

import com.bk.olympia.request.socket.SocketService;
import com.bk.olympia.type.ContentType;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.List;
import java.util.Optional;

import static com.bk.olympia.config.Constant.LOBBY_SCREEN;
import static com.bk.olympia.config.Constant.LOGIN_SCREEN;

public class HomeController extends ScreenService {
    @FXML
    TextField playerId;

    @FXML
    Text errorMessage;

    public void findPlayer(ActionEvent event) {
        SocketService.getInstance().invite(playerId.getText(),
                success -> Platform.runLater(() -> {
                    if (success.getContent().containsKey(ContentType.STATUS)) {
                        //join queue success
                        System.out.println("Finding another player...");
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Find random player");
                        alert.setHeaderText("Finding another player, please wait...");
                        alert.getButtonTypes().clear();

                        ButtonType cancel = new ButtonType("Cancel search");
                        alert.getButtonTypes().setAll(cancel);
                    } else {
                        //joined lobby
                        String lobbyId = success.getContent(ContentType.LOBBY_ID);
                        String lobbyName = success.getContent(ContentType.LOBBY_NAME);
                        List<String> lobbyParticipant = success.getContent(ContentType.LOBBY_PARTICIPANT);
                        //TODO to lobby screen
                    }
                }),
                errorMessage -> Platform.runLater(() ->
                        System.out.println("Login error: " + errorMessage.getErrorType())
                )
        );
    }

    public void invitePlayer(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Invite player");
        alert.setHeaderText("Waiting for response, please wait...");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().setAll(new ButtonType("Cancel invite"));

        //Delay để fake việc tìm player
        Timeline idleStage = new Timeline(new KeyFrame(Duration.seconds(3.0), new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event1) {
                alert.hide();
                changeScreen(event, LOBBY_SCREEN);
            }
        }));
        idleStage.setCycleCount(1);
        idleStage.play();
        Optional<ButtonType> option = alert.showAndWait();

        System.out.println("Waiting for response...");
    }

    public void signOut(ActionEvent event) {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Sign Out");
            alert.setHeaderText("Signing out of the game");
            alert.setContentText("Are you sure you want to sign out?");
            alert.getButtonTypes().clear();

            ButtonType signOut = new ButtonType("Sign out");
            ButtonType cancel = new ButtonType("Cancel");
            alert.getButtonTypes().addAll(signOut, cancel);

            Optional<ButtonType> option = alert.showAndWait();

            if (option.get() == signOut) {
                changeScreen(event, LOGIN_SCREEN);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideError() {
        errorMessage.setText("");
    }
}
