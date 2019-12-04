package com.bk.olympia.UIFx;

import javafx.event.ActionEvent;

import java.io.IOException;

import static com.bk.olympia.config.Constant.LOGIN_SCREEN;

public class RegisterController extends ScreenService {
    public void onPressCancel(ActionEvent event) {
        changeScreen(event, LOGIN_SCREEN);
    }

    public void onPressRegister(ActionEvent event) {
        //TODO: call api to register
        changeScreen(event, LOGIN_SCREEN);
    }
}
