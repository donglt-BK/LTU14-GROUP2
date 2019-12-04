package com.bk.olympia.UIFx;

import com.bk.olympia.message.ErrorMessage;
import com.bk.olympia.message.Message;
import com.bk.olympia.request.socket.ResponseHandler;
import com.bk.olympia.request.socket.SocketService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;


import java.awt.*;
import java.io.IOException;
import java.net.Socket;

import static com.bk.olympia.config.Constant.LOGIN_SCREEN;
import static com.bk.olympia.config.MultiUseFunc.*;



public class RegisterController extends ScreenService {

    public class Gender {
        private String name;
        private int value;
        public int getValue(){
            return value;
        }

        public String getName() {
            return name;
        }
        public Gender(String name, int value){
            this.name = name;
            this.value = value;
        }
    }

    @FXML
    TextField username;
    @FXML
    TextField name;
    @FXML
    TextField email;
    @FXML
    PasswordField password;
    @FXML
    Label message;
    @FXML
    ComboBox<Gender> gender;

    /**
     * This method is called when component is mounted
     */
    public void initialize(){
        message.setTextFill(Color.RED);
        Gender male = new Gender("Male", 1);
        Gender female = new Gender("Female", 2);
        Gender other = new Gender("Other", 3);
        gender.getItems().addAll(male, female, other);
        gender.setConverter(new StringConverter<Gender>() {
            @Override
            public String toString(Gender object) {
                return object.getName();
            }

            @Override
            public Gender fromString(String string) {
                return null;
            }
        });
    }
    public void onPressCancel(ActionEvent event) throws IOException {
        onChangeScreen(event, LOGIN_SCREEN);
    }
    public void onPressRegister(ActionEvent event){
        try{
            //TODO: call api to register
            String usernameInput = username.getText(),
                    nameInput = name.getText(),
                    emailInput = email.getText(),
                    passwordInput = password.getText();
            int genderInput = gender.getValue().getValue();
            if(!isNullOrEmpty(usernameInput) && !isNullOrEmpty(nameInput)&& !isNullOrEmpty(emailInput) && !isNullOrEmpty(passwordInput)){
                System.out.println("Calling API...");
                SocketService.getInstance().signUp(usernameInput, passwordInput, nameInput, genderInput, new ResponseHandler() {
                    @Override
                    public void success(Message responseMessage) {
                        try{
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Registration complete");
                            alert.setContentText("Your account has been created successfully. Now you can login using this account!");
                            onChangeScreen(event, LOGIN_SCREEN);
                        }
                        catch (Exception ex){
                            message.setText("Something went wrong with the registration...");
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void error(ErrorMessage errorMessage) {

                    }
                });
            }
            else{
                message.setText("You must fill in all the fields!");
                return;
            }
            onChangeScreen(event, LOGIN_SCREEN);
        }
        catch (Exception ex){
            ex.printStackTrace();
            message.setText("There's something wrong...");
            return;
        }
    }
}
