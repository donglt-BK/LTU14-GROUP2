package com.bk.olympia.UI.screen;

import com.bk.olympia.exception.ScreenNotFoundException;
import com.bk.olympia.message.Message;
import com.bk.olympia.request.socket.ResponseHandler;
import com.bk.olympia.request.socket.SocketService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static com.bk.olympia.config.Constant.*;

public class LoginScreen extends Screen {
    private JLabel errorNotification;
    private JButton loginBtn;
    private JButton registerBtn;
    private JTextField user;
    private JPasswordField pass;

    public LoginScreen() {
        super(LOGIN_SCREEN);
    }

    @Override
    public void generate(int windowWidth, int windowHeight) {
        this.removeAll();
        this.setBounds(0, 0, windowWidth, windowHeight);
        this.setLayout(null);
        this.setBackground(Color.BLACK);

        Color gray = new Color(210, 210, 210);

        //loginBtn button
        loginBtn = new JButton("Login");
        loginBtn.setBounds(230, 300, 140, 30);
        loginBtn.setEnabled(true);
        loginBtn.addActionListener((e) -> {
            String userInput = user.getText();
            if (userInput != null && userInput.trim().length()>0){
             login();
            }
            else {
                errorNotification.setText("Vui lòng nhập tài khoản và mật khẩu");
                errorNotification.setEnabled(true);
            }
        });

        //register
        registerBtn = new JButton("Register");
        registerBtn.setBounds(230, 350, 140, 30);
        registerBtn.setEnabled(true);
        //error message
        errorNotification = new JLabel();
        errorNotification.setBounds(150, 160, 300, 25);
        errorNotification.setForeground(Color.RED);

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

        this.add(errorNotification);
        this.add(userLabel);
        this.add(user);
        this.add(passLabel);
        this.add(pass);
        this.add(loginBtn);
        this.add(registerBtn);

        KeyListener submitEnterKeyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    login();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        };

        user.addKeyListener(submitEnterKeyListener);
        pass.addKeyListener(submitEnterKeyListener);

//        loginBtn.addActionListener(actionEvent -> login());
        registerBtn.addActionListener(actionEvent -> register());
    }

    private void register() {

    }

    private void login() {

        SocketService.getInstance().login(user.getText(), String.valueOf(pass.getPassword()), new ResponseHandler() {
            @Override
            public void success(Object response) {
                try {
                    System.out.println(">>" + response);
                    ui.showScreen(HOME_SCREEN);
                } catch (ScreenNotFoundException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void error(String errorMessage) {
                errorNotification.setText("Login failed");
                System.out.println(errorMessage);
                System.out.println("Login failed");
            }
        });
    }
}
