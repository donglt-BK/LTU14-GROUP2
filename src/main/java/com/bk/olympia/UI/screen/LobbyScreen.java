package com.bk.olympia.UI.screen;

import java.awt.*;

import static com.bk.olympia.config.Constant.*;

public class LobbyScreen extends Screen {

    public LobbyScreen() {
        super(LOBBY_SCREEN);
    }

    @Override
    public void generate(int windowWidth, int windowHeight) {
        this.setBounds(0, 0, windowWidth, windowHeight);
        this.setLayout(new GridLayout(1, 3));
        this.setBackground(Color.BLACK);

    }
}
