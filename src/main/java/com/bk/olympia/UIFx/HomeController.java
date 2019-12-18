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
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.bk.olympia.config.Constant.*;

public class HomeController extends ScreenService {
    public Text userBalance;
    public Label nameLabel;
    public TextField playerName;

    @FXML
    Text errorMessage;

    public void initialize() {
        int curBalance = UserSession.getInstance().getBalance();
        userBalance.setText(String.valueOf(curBalance));
        nameLabel.setText("Hello " + UserSession.getInstance().getName());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Thread t = new Thread(() -> SocketService.getInstance().subscribeInvite(
                message -> Platform.runLater(() -> {
                    if (message.getContent(ContentType.REPLY) != null) {
                        inviteWaiting.hide();
                        if (message.getContent(ContentType.REPLY)) {
                            changeScreen((Stage) nameLabel.getScene().getWindow(), LOBBY_SCREEN);
                        } else {
                            alert.setTitle("Invite player");
                            alert.setHeaderText(message.getContent(ContentType.NAME) + " decline your invitation");
                            alert.getButtonTypes().clear();
                            ButtonType cancel = new ButtonType("Back");
                            alert.getButtonTypes().setAll(cancel);

                            alert.showAndWait();
                            alert.hide();
                        }
                    } else {
                        alert.setTitle("Player invitation");
                        alert.setHeaderText("You have receipt invitation from " + message.getContent(ContentType.USERNAME));

                        alert.getButtonTypes().clear();
                        ButtonType cancel = new ButtonType("Decline");
                        ButtonType accept = new ButtonType("Accept");
                        alert.getButtonTypes().setAll(cancel, accept);

                        Optional<ButtonType> option = alert.showAndWait();
                        if (option.get() == cancel) {
                            message.addContent(ContentType.REPLY, false);
                            SocketService.getInstance().replyInvite(message, error -> {
                            });
                            alert.hide();
                        } else {
                            changeScreen((Stage) nameLabel.getScene().getWindow(), LOBBY_SCREEN);
                            message.addContent(ContentType.REPLY, true);
                            SocketService.getInstance().replyInvite(message, error -> {
                            });
                            alert.hide();
                        }
                    }
                }), error -> {
                }
        ));
        t.start();
    }

    public void findPlayer(ActionEvent event) {
        changeScreen(event, LOBBY_SCREEN);
    }

    private Alert inviteWaiting = new Alert(Alert.AlertType.INFORMATION);
    public void invitePlayer(ActionEvent event) {
        inviteWaiting.setTitle("Invite player");
        inviteWaiting.setHeaderText("Waiting for response, please wait...");
        inviteWaiting.getButtonTypes().clear();
        ButtonType cancel = new ButtonType("Cancel invite");
        inviteWaiting.getButtonTypes().setAll(cancel);
        Thread t = new Thread(() -> SocketService.getInstance().invite(playerName.getText(), error -> {
        }));
        t.start();
        inviteWaiting.show();
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
