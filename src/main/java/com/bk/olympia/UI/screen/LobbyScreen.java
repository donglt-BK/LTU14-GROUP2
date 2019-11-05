package com.bk.olympia.UI.screen;

import com.bk.olympia.exception.ScreenNotFoundException;

import javax.swing.*;
import java.awt.*;

import static com.bk.olympia.config.Constant.*;

public class LobbyScreen extends Screen {
    private JButton readyBtn = new JButton("Ready!");
    private JButton leaveBtn = new JButton("Leave!");

    private String[] betArray = {"100", "500", "1000"};
    private JComboBox bet = new JComboBox(betArray);


    public LobbyScreen() {
        super(LOBBY_SCREEN);
    }

    @Override
    public void generate(int windowWidth, int windowHeight) {
        this.removeAll();
        this.setBounds(0, 0, windowWidth, windowHeight);
        this.setLayout(null);
        this.setBackground(gray);

        readyBtn.setBounds(230, 250, 140, 30);
        leaveBtn.setBounds(230, 300, 140, 30);

        readyBtn.setEnabled(true);
        leaveBtn.setEnabled(true);

        this.add(readyBtn);
        this.add(leaveBtn);
        this.add(bet);

        readyBtn.addActionListener(e -> onReadyPressed());
        leaveBtn.addActionListener(e -> onLeavePressed(this));
    }

    private void onReadyPressed() {
        readyBtn.setEnabled(false);
    }
    private void onLeavePressed(LobbyScreen lobbyScreen) {
        int result = JOptionPane.showConfirmDialog(lobbyScreen, "Do you want to leave", "Confirm Leave", JOptionPane.OK_CANCEL_OPTION);
        if (result == 0) {
            try{
                ui.showScreen(HOME_SCREEN);
            }catch (ScreenNotFoundException e){
                e.printStackTrace();
            }
        }
    }
}
