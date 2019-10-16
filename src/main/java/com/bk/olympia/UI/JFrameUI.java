package com.bk.olympia.UI;

import com.bk.olympia.UI.screen.Screen;
import com.bk.olympia.exception.ScreenNotFoundException;
import com.sun.javaws.util.JfxHelper;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JFrameUI {
    private static JFrameUI instance;

    private JFrame jFrame;
    private Map<String, Screen> screens;
    private String currentScreen;

    public static JFrameUI getInstance() {
        if (instance == null) instance = new JFrameUI();
        return instance;
    }

    private JFrameUI() {
        jFrame = new JFrame();
        screens = new HashMap<>();
    }

    public JFrameUI config(String title, int windowWidth, int windowHeight) {
        jFrame.setTitle(title);
        jFrame.setSize(windowWidth, windowHeight);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setResizable(false);
        jFrame.setVisible(true);

        return instance;
    }

    public void addScreen(List<Screen> screens) {
        for (Screen screen : screens) {
            this.screens.put(screen.getScreenName(), screen);
        }
    }

    public void showScreen(String screenName) throws ScreenNotFoundException {
        if (!screens.containsKey(screenName)) {
            throw new ScreenNotFoundException();
        }
        if (currentScreen != null) {
            jFrame.remove(screens.get(currentScreen));
        }
        jFrame.add(screens.get(screenName));
        jFrame.validate();
        jFrame.repaint();

        currentScreen = screenName;
    }
}
