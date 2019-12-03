package com.bk.olympia.UIFx;

import javafx.event.ActionEvent;

import java.io.IOException;

import static com.bk.olympia.config.Constant.LOGIN_SCREEN;

public class RegisterController extends ScreenService {
    public void onPressCancel(ActionEvent event) throws IOException {
        onChangeScreen(event, LOGIN_SCREEN);
    }
    public void onPressRegister(ActionEvent event){
        try{
            //TODO: call api to register
            onChangeScreen(event, LOGIN_SCREEN);
        }
        catch (IOException ex){
            System.out.print(ex.getStackTrace());
        }
    }
}
