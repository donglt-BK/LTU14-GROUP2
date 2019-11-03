package com.bk.olympia.UI.screen;

import javax.swing.*;
import java.awt.*;

import static com.bk.olympia.config.Constant.*;

public class HomeScreen extends Screen {
    private static JButton findGame = new JButton("Find match");
    private static JButton invite = new JButton("Invite a player");

    public HomeScreen() {
        super(HOME_SCREEN);
    }

    @Override
    public void generate(int windowWidth, int windowHeight) {
        this.setBounds(0, 0, windowWidth, windowHeight);
        this.setLayout(new GridLayout(1, 3));
        this.setBackground(Color.gray);

        invite.setBounds(this.getWidth()/2, this.getHeight()/2+210, 200, 200);
        findGame.setBounds(this.getWidth()/2, this.getHeight()/2, 200, 200);
        this.add(findGame);
        this.add(invite);
    }
}
