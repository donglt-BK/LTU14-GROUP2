package com.bk.olympia.UIFx;

import com.bk.olympia.request.socket.SocketService;
import com.bk.olympia.type.ContentType;
import com.bk.olympia.model.History;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

import java.util.ArrayList;
import java.util.List;

import static com.bk.olympia.config.Constant.HOME_SCREEN;

public class HistoryController extends ScreenService {
    public ScrollPane historyPane;

    public void onPressGoback(ActionEvent event) {
        changeScreen(event, HOME_SCREEN);
    }

    public void initialize() {
        Thread t = new Thread(() ->
                SocketService.getInstance().getHistory(
                        message -> Platform.runLater(() -> {
                            History.getInstance().setRoomId(message.getContent(ContentType.HISTORY_ROOM_ID));
                            History.getInstance().setRoomId(message.getContent(ContentType.HISTORY_CREATED_AT));
                            History.getInstance().setRoomId(message.getContent(ContentType.HISTORY_ENDED_AT));
                            History.getInstance().setRoomId(message.getContent(ContentType.HISTORY_RESULT_TYPE));
                            History.getInstance().setRoomId(message.getContent(ContentType.HISTORY_BALANCE_CHANGED));
                        }),
                        error -> {
                        }

                )
        );
        t.start();
    }

}
