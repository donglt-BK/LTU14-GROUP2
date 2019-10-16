package com.bk.olympia.UI.screen;

import com.bk.olympia.UI.JFrameUI;

import javax.swing.*;

public class Screen extends JPanel {
    protected String screenName;
    protected JFrameUI ui;

    protected Screen(String screenName) {
        this.screenName = screenName;
        ui = JFrameUI.getInstance();
    }

    public String getScreenName() {
        return screenName;
    }
}
