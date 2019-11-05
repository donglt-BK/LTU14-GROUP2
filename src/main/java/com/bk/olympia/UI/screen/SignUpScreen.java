package com.bk.olympia.UI.screen;

import com.bk.olympia.exception.ScreenNotFoundException;

import javax.swing.*;
import java.awt.*;

import static com.bk.olympia.config.Constant.*;
import static com.bk.olympia.config.MultiUseFunc.isNullOrEmpty;

public class SignUpScreen extends Screen {
    private JTextField user;
    private JPasswordField pass;
    private JTextField name;
    String[] genderString = {"Nam", "Nữ", "Khác"};
    private JComboBox gender = new JComboBox(genderString);
    private JButton backBtn;
    private JButton registerBtn;
    private JLabel errorNotification;



    public SignUpScreen() {
        super(SIGNUP_SCREEN);
    }

    @Override
    public void generate(int windowWidth, int windowHeight) {
        this.removeAll();
        this.setBounds(0, 0, windowWidth, windowHeight);
        this.setLayout(null);
        this.setBackground(Color.BLACK);

        //username
        JLabel userLabel = new JLabel("Username: ");
        userLabel.setBounds(150, 200, 100, 25);
        userLabel.setForeground(gray);

        user = new JTextField();
        user.setBounds(250, 200, 200, 25);
        user.setBackground(gray);

        //password
        JLabel passLabel = new JLabel("Password: ");
        passLabel.setBounds(150, 250, 100, 25);
        passLabel.setForeground(gray);

        pass = new JPasswordField();
        pass.setBounds(250, 250, 200, 25);
        pass.setBackground(gray);

        //name
        JLabel nameLabel = new JLabel("Name: ");
        nameLabel.setBounds(150, 300, 100, 25);
        nameLabel.setForeground(gray);

        name = new JTextField();
        name.setBounds(250, 300, 200, 25);
        name.setBackground(gray);

        //gender
        JLabel genderLabel = new JLabel("Sex: ");
        genderLabel.setBounds(150, 350, 100,25);
        genderLabel.setForeground(gray);

        gender.setSelectedIndex(0);
        gender.setBackground(gray);
        gender.setBounds(250, 350, 200, 25);
        gender.setEnabled(true);

        //register
        registerBtn = new JButton("Register");
        registerBtn.setBounds(230, 400, 140, 30);
        registerBtn.setEnabled(true);
        //error message
        errorNotification = new JLabel();
        errorNotification.setBounds(150, 160, 300, 25);
        errorNotification.setForeground(Color.RED);
        //goBack
        backBtn=new JButton("<- Back");
        backBtn.setBounds(10, 20, 140, 30);
        backBtn.setEnabled(true);

        //add-to-screen
        this.add(errorNotification);
        this.add(userLabel);
        this.add(user);
        this.add(passLabel);
        this.add(pass);
        this.add(nameLabel);
        this.add(name);
        this.add(genderLabel);
        this.add(gender);
        this.add(registerBtn);
        this.add(backBtn);

        //event-handling
        registerBtn.addActionListener(e -> checkForm());
        backBtn.addActionListener(e -> goBack());
    }

    private void goBack() {
        try{
            ui.showScreen(LOGIN_SCREEN);
            clearInputs();
        }catch (ScreenNotFoundException e){
            e.printStackTrace();
        }
    }

    private void checkForm() {
        String userInput = user.getText(),
                nameInput = name.getText();
        if (!isNullOrEmpty(userInput) && !isNullOrEmpty(nameInput)) {
            register();
        }
        else {
            errorNotification.setText("Vui lòng hoàn thành form theo hướng dẫn");
        }
    }

    private void register() {
        try{
            ui.showScreen(HOME_SCREEN);
            clearInputs();
        }
        catch (ScreenNotFoundException e){
            e.printStackTrace();
        }
//        SocketService.getInstance().signUp(user.getText(), String.valueOf(pass.getPassword()), name.getText(), gender.getSelectedIndex(), new ResponseHandler() {
//            @Override
//            public void success(Object response) {
//                try {
//                    System.out.println(">>" + response);
//                    ui.showScreen(HOME_SCREEN);
//                } catch (ScreenNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void error(String errorMessage) {
//                errorNotification.setText("Login failed");
//                System.out.println(errorMessage);
//                System.out.println("Login failed");
//            }
//        });
    }

    private void clearInputs() {
        user.setText(EMPTY_STRING);
        pass.setText(EMPTY_STRING);
        name.setText(EMPTY_STRING);
    }
}
