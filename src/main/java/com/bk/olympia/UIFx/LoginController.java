package com.bk.olympia.UIFx;

import com.bk.olympia.message.ErrorMessage;
import com.bk.olympia.message.Message;
import com.bk.olympia.request.socket.ResponseHandler;
import com.bk.olympia.request.socket.SocketService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import static com.bk.olympia.config.Constant.LOBBY_SCREEN;
import static com.bk.olympia.config.Constant.SIGNUP_SCREEN;
import static com.bk.olympia.config.MultiUseFunc.*;

public class LoginController extends ScreenService {
    @FXML
    TextField username;
    @FXML
    PasswordField password;

    @FXML
    public void onPressRegister(ActionEvent event) throws Exception {
        onChangeScreen(event, SIGNUP_SCREEN);
    }

    public void onPressLogin(ActionEvent event)  {
        String userInput = username.getText(),
                passwordInput = password.getText();
        if (!isNullOrEmpty(userInput) && !isNullOrEmpty(passwordInput)) {
            SocketService.getInstance().login(userInput, passwordInput, new ResponseHandler() {
                @Override
                public void success(Message responseMessage) {
                    try {
                        onChangeScreen(event, LOBBY_SCREEN);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void error(ErrorMessage errorMessage) {
                    System.out.println("Error login: " + errorMessage.getErrorType());
                }
            });
        }
    }
}

