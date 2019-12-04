package com.bk.olympia.UIFx;

import com.bk.olympia.request.socket.SocketService;
import com.bk.olympia.type.ErrorType;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import static com.bk.olympia.config.Constant.LOBBY_SCREEN;
import static com.bk.olympia.config.Constant.SIGNUP_SCREEN;
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
        changeScreen(event, SIGNUP_SCREEN);
    }

    public void clearErrorMessage() {
        errorMessage.setText("");
    }

    public void onPressLogin(Event event) {
        String userInput = username.getText(),
                passwordInput = password.getText();
        if (!isNullOrEmpty(userInput) && !isNullOrEmpty(passwordInput)) {
            SocketService.getInstance().login(userInput, passwordInput,
                    response -> Platform.runLater(() -> changeScreen(event, LOBBY_SCREEN)),
                    error -> Platform.runLater(() -> {
                        if (error.getErrorType() == ErrorType.AUTHENTICATION) {
                            errorMessage.setText("Wrong username or password");
                        } else {
                            //showError("Something went wrong! :(","Login error: " + error.getErrorType() );
                            errorMessage.setText("Something went wrong! :(");
                            System.out.println("Login error: " + error.getErrorType());
                        }
                    })
            );
        }
        else {
            errorMessage.setText("Missing username or password");
        }
    }

    public void textFieldChange(KeyEvent keyEvent) {
        if (keyEvent.getCharacter().equals("\r")) {
            onPressLogin(keyEvent);
        };
    }
}

