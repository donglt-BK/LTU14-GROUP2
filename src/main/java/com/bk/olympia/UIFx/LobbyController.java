package com.bk.olympia.UIFx;

import com.bk.olympia.model.UserSession;
import com.bk.olympia.request.socket.SocketService;
import com.bk.olympia.type.ContentType;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import static com.bk.olympia.config.Constant.GAME_SCREEN;
import static com.bk.olympia.config.Constant.HOME_SCREEN;

public class LobbyController extends ScreenService {
    public Label your_ready, your_opp_ready;
    public Button readyBtn, startBtn;
    private static boolean isReady = false;
    public Hyperlink back_home;

    public void onPressReady(ActionEvent event) {
        Paint yourReady = your_ready.getTextFill(),
                hisReady = your_opp_ready.getTextFill();
        int playerId = UserSession.getInstance().getUserId();
        if (!isReady) {
            isReady = true;
            your_ready.setTextFill(Color.GREEN);
            your_ready.setText("Ready!");
            readyBtn.setText("I'm not ready");
        } else {
            isReady = false;
            your_ready.setTextFill(Color.RED);
            your_ready.setText("Not ready");
            readyBtn.setText("Ready!");
        }
        Thread t = new Thread(() -> SocketService.getInstance().ready(
                response -> {
                    boolean isAlpha = UserSession.getInstance().isAlpha();
                    if (hisReady.equals(Color.GREEN) && isAlpha) {
                        startBtn.setVisible(true);
                    } else {
                        startBtn.setVisible(false);
                    }
                },
                error -> {
                    showError("Failed!", "Check your connection to server!");
                }
        ));
        t.start();
    }

    public void onPressStart(ActionEvent event) {
        SocketService.getInstance().start(
                success -> {
                    UserSession.getInstance().setRoomId(success.getContent(ContentType.ROOM_ID));
                    changeScreen(event, GAME_SCREEN);
                },
                error -> {
                    showError("Failed!", "Check your connection to server!");
                }
        );
    }

    public void backToHome(ActionEvent event) {
        changeScreen(event, HOME_SCREEN);
    }
}
