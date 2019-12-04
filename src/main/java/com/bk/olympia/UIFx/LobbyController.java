package com.bk.olympia.UIFx;

import com.bk.olympia.model.UserSession;
import com.bk.olympia.request.socket.SocketService;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import static com.bk.olympia.config.Constant.GAME_SCREEN;

public class LobbyController extends ScreenService {
    public Label your_ready, your_opp_ready;
    public Button readyBtn, startBtn;

    public void onPressReady(ActionEvent event) {
        Boolean yourReady = your_ready.isVisible(),
                hisReady = your_opp_ready.isVisible();
        int playerId = UserSession.getInstance().getUserId();
        if (!yourReady) {
            your_ready.setVisible(true);
            readyBtn.setText("Not Ready?");
            SocketService.getInstance().ready(String.valueOf(playerId),
                    response -> {
                        Boolean isAlpha = UserSession.getInstance().isAlpha();
                        if (!hisReady && isAlpha) {
                            startBtn.setVisible(true);
                        } else {
                            startBtn.setVisible(false);
                        }
                    },
                    error -> {
                        showError("Ready Failed!", "Check your connection to server!");
                    }
            );
        } else {
            //TODO: send API unready
            your_ready.setVisible(false);
            readyBtn.setText("Ready!");
        }
    }

    public void onPressStart(ActionEvent event) {
        //TODO: add send game func
        changeScreen(event, GAME_SCREEN);
    }
}
