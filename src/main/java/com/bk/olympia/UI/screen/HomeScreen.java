package com.bk.olympia.UI.screen;

import com.bk.olympia.exception.ScreenNotFoundException;

import javax.swing.*;
import java.awt.*;

import static com.bk.olympia.config.Constant.*;
import static com.bk.olympia.config.MultiUseFunc.*;

public class HomeScreen extends Screen {
    private static JButton findBtn = new JButton("Find match");
    private static JButton inviteBtn = new JButton("Invite a player");
    private static JButton logoutBtn = new JButton("Sign out");
    private static JLabel welcomeLabel = new JLabel("Welcome, fucker!");
    private static JOptionPane invitePane = new JOptionPane();

    public HomeScreen() {
        super(HOME_SCREEN);
    }

    @Override
    public void generate(int windowWidth, int windowHeight) {
        this.removeAll();
        this.setBounds(0, 0, windowWidth, windowHeight);
        this.setLayout(null);
        this.setBackground(Color.gray);

        welcomeLabel.setBounds(230, 250, 140, 30);

        findBtn.setBounds(230, 300, 140, 30);
        findBtn.setEnabled(true);

        inviteBtn.setBounds(230, 350, 140, 30);
        inviteBtn.setEnabled(true);

        logoutBtn.setBounds(10,20,140,30);
        logoutBtn.setEnabled(true);

        this.add(findBtn);
        this.add(inviteBtn);
        this.add(logoutBtn);
        this.add(welcomeLabel);
        this.add(invitePane);

        logoutBtn.addActionListener(e -> checkLogout());
        inviteBtn.addActionListener(e -> onInvitePlayer(this));
    }

    private void onInvitePlayer(HomeScreen that) {
        String playerId = invitePane.showInputDialog(that, "Enter player Id: ", "Invite Your Friend", JOptionPane.OK_CANCEL_OPTION);
        if (!isNullOrEmpty(playerId)) {
            invitePlayer(playerId);
        }
    }
    private void invitePlayer(String playerId) {
        System.out.println(playerId);
    }

    private void checkLogout() {
        logout();
    }
    private void logout() {
        try{
            ui.showScreen(LOGIN_SCREEN);
        }catch (ScreenNotFoundException e){
            e.printStackTrace();
        }
    }
}
