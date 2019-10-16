package com.bk.olympia.UI.screen;

import com.bk.olympia.UI.JFrameUI;

import javax.swing.*;

public abstract class Screen extends JPanel {
    protected String screenName;
    protected JFrameUI ui;

    protected Screen(String screenName) {
        this.screenName = screenName;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setJFrameUI(JFrameUI jFrameUI) {
        ui = jFrameUI;
    }

    public abstract void generate(int windowWidth, int windowHeight);
}
