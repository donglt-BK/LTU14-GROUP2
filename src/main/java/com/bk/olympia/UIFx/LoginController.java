package com.bk.olympia.UIFx;

import com.bk.olympia.model.UserSession;
import com.bk.olympia.request.socket.SocketService;
import com.bk.olympia.type.ContentType;
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
        changeScreen(event, SIGNUP_SCREEN);
    }

    public void clearErrorMessage() {
        errorMessage.setText("");
    }

    private boolean blockUI = false;

    public void onPressLogin(Event event) {
        if (!blockUI) {
            blockUI = true;
            String userInput = username.getText(),
                    passwordInput = password.getText();
            if (!isNullOrEmpty(userInput) && !isNullOrEmpty(passwordInput)) {
                SocketService.getInstance().login(userInput, passwordInput,
                        response -> Platform.runLater(() -> {
                            blockUI = false;
                            UserSession.getInstance().setUserId(response.getContent(ContentType.USER_ID));
                            SocketService.getInstance().getUserInfo(
                                    success -> {
                                        UserSession.getInstance().setData(success.getContent());
                                    },
                                    errorMessage -> System.out.println(errorMessage.getErrorType())
                            );
                            changeScreen(event, LOBBY_SCREEN);
                        }),
                        error -> Platform.runLater(() -> {
                            blockUI = false;
                            if (error.getErrorType() == ErrorType.AUTHENTICATION) {
                                errorMessage.setText("Wrong username or password");
                            } else {
                                //showError("Something went wrong! :(","Login error: " + error.getErrorType() );
                                errorMessage.setText("Something went wrong! :(");
                                System.out.println("Login error: " + error.getErrorType());
                            }
                        })
                );
            } else {
                blockUI = false;
                errorMessage.setText("Missing username or password");
            }
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
}

