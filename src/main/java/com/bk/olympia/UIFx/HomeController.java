package com.bk.olympia.UIFx;

import com.bk.olympia.model.UserSession;
import com.bk.olympia.request.socket.SocketService;
import com.bk.olympia.type.ContentType;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.bk.olympia.config.Constant.*;

public class HomeController extends ScreenService {
    public Text userBalance;
    public Label nameLabel;
    @FXML
    TextField playerId;

    @FXML
    Text errorMessage;

    public void initialize() {
        int curBalance = UserSession.getInstance().getBalance();
        userBalance.setText(String.valueOf(curBalance));
        nameLabel.setText("Hello " + UserSession.getInstance().getName());
    }

    public void findPlayer(ActionEvent event) {
        changeScreen(event, LOBBY_SCREEN);
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

    public void nextScene(ActionEvent event) {
        changeScreen(event, LOBBY_SCREEN);
    }

    public void onPressHistory(ActionEvent event) {
        changeScreen(event, HISTORY_SCREEN);
    }
}
