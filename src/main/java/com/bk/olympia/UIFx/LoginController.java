package com.bk.olympia.UIFx;

import com.bk.olympia.message.ErrorMessage;
import com.bk.olympia.request.socket.SocketService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import static com.bk.olympia.config.Constant.LOBBY_SCREEN;
import static com.bk.olympia.config.Constant.SIGNUP_SCREEN;
import static com.bk.olympia.config.MultiUseFunc.*;

public class LoginController extends ScreenService {
    @FXML
    TextField username;
    @FXML
    PasswordField password;

    @FXML
    public void onPressRegister(ActionEvent event) {
        changeScreen(event, SIGNUP_SCREEN);
    }

    public void onPressLogin(ActionEvent event) {
        String userInput = username.getText(),
                passwordInput = password.getText();
        if (!isNullOrEmpty(userInput) && !isNullOrEmpty(passwordInput)) {
            SocketService.getInstance().login(userInput, passwordInput,
                    response -> changeScreen(event, LOBBY_SCREEN),
                    errorMessage -> System.out.println("Error login: " + ((ErrorMessage) errorMessage).getErrorType())
            );
        }
    }
}

