package com.bk.olympia.UIFx;

import javafx.scene.control.Label;

public class LobbyController extends ScreenService {
    public Label your_ready;

    public void onPressReady(){
        your_ready.setVisible(true);
}
}
