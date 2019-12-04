package com.bk.olympia.UIFx;

import com.bk.olympia.model.UserSession;
import com.bk.olympia.request.socket.SocketService;
import com.bk.olympia.type.ContentType;
import com.bk.olympia.type.ErrorType;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import static com.bk.olympia.config.Constant.*;
import static com.bk.olympia.config.Util.*;

public class LoginController extends ScreenService {
    @FXML
    TextField username;
    @FXML
    PasswordField password;
    @FXML
    Label errorMessage;
    @FXML
    Button login;

    public void onPressRegister(ActionEvent event) {
        changeScreen(event, SIGN_UP_SCREEN);
    }

    public void clearErrorMessage() {
        errorMessage.setText("");
    }

    public void onPressLogin(Event event) {
        login.setDisable(true);
        String userInput = username.getText(),
                passwordInput = password.getText();
        if (!isNullOrEmpty(userInput) && !isNullOrEmpty(passwordInput)) {
            Thread t = new Thread(() -> {
                SocketService.getInstance().login(userInput, passwordInput,
                        response -> {
                            UserSession.getInstance().setUserId(response.getContent(ContentType.USER_ID));
                            SocketService.getInstance().getUserInfo(
                                    success -> Platform.runLater(() -> {
                                        UserSession.getInstance().setData(success.getContent());
                                        changeScreen(event, HOME_SCREEN);
                                    }),
                                    errorMessage -> {
                                        login.setDisable(false);
                                        System.out.println(errorMessage.getErrorType());
                                    }
                            );
                        },
                        error -> Platform.runLater(() -> {
                            login.setDisable(false);
                            if (error.getErrorType() == ErrorType.AUTHENTICATION) {
                                errorMessage.setText("Wrong username or password");
                            } else {
                                errorMessage.setText("Something went wrong! :(");
                                System.out.println("Login error: " + error.getErrorType());
                            }
                        })
                );
            });
            t.start();
        } else {
            login.setDisable(false);
            errorMessage.setText("Missing username or password");
        }
    }

    public void toHome(ActionEvent event) {
        changeScreen(event, HOME_SCREEN);
    }

    public void textFieldChange(KeyEvent keyEvent) {
        if (keyEvent.getCharacter().equals("\r")) {
            onPressLogin(keyEvent);
        }
        ;
    }

    public void nextScene(ActionEvent event) {
        changeScreen(event, HOME_SCREEN);
    }
}

