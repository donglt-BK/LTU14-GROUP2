package com.bk.olympia.UIFx;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class LobbyController extends ScreenService {
    public Label your_ready;
    public Button readyBtn;

    public void onPressReady(ActionEvent event) {
        Boolean ready = your_ready.isVisible();
        if (!ready) {
            your_ready.setVisible(true);
            readyBtn.setText("Not Ready?");
        } else {
            your_ready.setVisible(false);
            readyBtn.setText("Ready!");
        }
        //TODO: add send ready func
    }
}
