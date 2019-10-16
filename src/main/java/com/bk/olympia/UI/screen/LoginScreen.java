package com.bk.olympia.UI.screen;

import com.bk.olympia.exception.ScreenNotFoundException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static com.bk.olympia.config.Constant.*;

public class LoginScreen extends Screen {
    private JLabel errorNotification;
    private JButton submit;
    private JTextField user;
    private JPasswordField pass;

    public LoginScreen(int windowWidth, int windowHeight) {
        super("LOGIN");

        this.setBounds(0, 0, windowWidth, windowHeight);
        this.setLayout(null);
        this.setBackground(Color.BLACK);

        Color gray = new Color(210, 210, 210);

        //submit button
        submit = new JButton("Login");
        submit.setBounds(230, 300, 140, 30);
        submit.setEnabled(false);

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

        KeyListener submitEnterKeyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    try {
                        ui.showScreen("HOME");
                    } catch (ScreenNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        };

        user.addKeyListener(submitEnterKeyListener);
        pass.addKeyListener(submitEnterKeyListener);

        submit.addActionListener(actionEvent -> {
            try {
                ui.showScreen(SCREEN_HOME);
            } catch (ScreenNotFoundException ex) {
                ex.printStackTrace();
            }
        });

    }
}
