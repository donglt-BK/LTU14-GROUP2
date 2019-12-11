package com.bk.olympia.UIFx;

import com.bk.olympia.request.socket.SocketService;
import com.bk.olympia.type.ContentType;
import com.bk.olympia.model.History;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.bk.olympia.config.Constant.HOME_SCREEN;

public class HistoryController extends ScreenService {
    public Label noDataMessage;
    private int[] roomId;
    private LocalDateTime[] createdAt;
    private LocalDateTime[] endedAt;
    private String[] resultType;
    private int[] balanceChanged;
    public ScrollPane historyPane;

    private final VBox historyBox = new VBox(5);

    public void onPressGoback(ActionEvent event) {
        changeScreen(event, HOME_SCREEN);
    }

    public void initialize() {
        historyPane.setContent(historyBox);

        SocketService.getInstance().getHistory(
            message -> {
                roomId = message.getContent(ContentType.HISTORY_ROOM_ID);
                createdAt = message.getContent(ContentType.HISTORY_CREATED_AT);
                endedAt = message.getContent(ContentType.HISTORY_ENDED_AT);
                resultType = message.getContent(ContentType.HISTORY_RESULT_TYPE);
                balanceChanged = message.getContent(ContentType.HISTORY_BALANCE_CHANGED);

                historyPane.setDisable(false);
                noDataMessage.setDisable(true);
                for (int i = 0; i < roomId.length; i++) {
                    HBox historyRow = new HBox(10);
                    historyBox.getChildren().add(historyRow);
                    historyRow.getChildren().add(new Label(String.valueOf(roomId[i])));
                    historyRow.getChildren().add(new Label(String.valueOf(createdAt[i])));
                    historyRow.getChildren().add(new Label(String.valueOf(endedAt[i])));
                    historyRow.getChildren().add(new Label(String.valueOf(resultType[i])));
                    historyRow.getChildren().add(new Label(String.valueOf(balanceChanged[i])));
                }
            },
            error -> {
                System.out.print("Error in get history");
                historyPane.setDisable(true);
                noDataMessage.setDisable(false);
            }
        );
    }
}
