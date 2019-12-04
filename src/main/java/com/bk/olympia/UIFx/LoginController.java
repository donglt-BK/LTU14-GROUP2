package com.bk.olympia.UIFx;

import com.bk.olympia.request.socket.SocketService;
import com.bk.olympia.type.ErrorType;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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
    public void onPressRegister(ActionEvent event) {
        changeScreen(event, SIGNUP_SCREEN);
    }

    public void onPressLogin(ActionEvent event) {
        String userInput = username.getText(),
                passwordInput = password.getText();
        if (!isNullOrEmpty(userInput) && !isNullOrEmpty(passwordInput)) {
            Platform.setImplicitExit(false);
            SocketService.getInstance().login(userInput, passwordInput,
                    response -> Platform.runLater(() -> changeScreen(event, LOBBY_SCREEN)),
                    error -> Platform.runLater(() ->{
                        if (error.getErrorType() == ErrorType.AUTHENTICATION) {
                            errorMessage.setText("Wrong password");
                        } else {
                            errorMessage.setText("Something went wrong! :(");
                            System.out.println("Login error: " + error.getErrorType());
                        }
                    })
            );
        }
    }

    public void toHome(ActionEvent event){
        changeScreen(event, HOME_SCREEN);
    }
}

