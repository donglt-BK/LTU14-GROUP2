package com.bk.olympia.UI.screen;

import com.bk.olympia.exception.ScreenNotFoundException;
import com.bk.olympia.message.ErrorMessage;
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
    private JButton submit;
    private JButton register;
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

        //submit button
        submit = new JButton("Login");
        submit.setBounds(230, 300, 140, 30);
        submit.setEnabled(false);

        //register
        register = new JButton("Register");
        register.setBounds(230, 350, 140, 30);
        register.setEnabled(false);
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
        this.add(submit);
        this.add(register);

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

        submit.addActionListener(actionEvent -> login());
    }

    private void login() {

        SocketService.getInstance().login(user.getText(), String.valueOf(pass.getPassword()), new ResponseHandler() {
            @Override
            public void success(Message response) {
                try {
                    ui.showScreen(HOME_SCREEN);
                } catch (ScreenNotFoundException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(ErrorMessage errorMessage) {
                System.out.println("Error");
                System.out.println("Error login: " + errorMessage.getErrorType());
            }
        });
    }
}
