package com.bk.olympia.UI.screen;

import java.awt.*;

import static com.bk.olympia.config.Constant.*;

public class HomeScreen extends Screen {
    public HomeScreen(int windowWidth, int windowHeight) {
        super(HOME_SCREEN);

        this.setBounds(0, 0, windowWidth, windowHeight);
        this.setLayout(new GridLayout(1,3));
        this.setBackground(Color.BLACK);
    }
}
