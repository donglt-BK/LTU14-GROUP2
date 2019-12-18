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
    private List<Double> roomId;
    private List<LocalDateTime> createdAt;
    private List<LocalDateTime> endedAt;
    private List<String> resultType;
    private List<Double> balanceChanged;
    public ScrollPane historyPane;

    private final VBox historyBox = new VBox(5);

    public void onPressGoback(ActionEvent event) {
        changeScreen(event, HOME_SCREEN);
    }

    public void initialize() {
        SocketService.getInstance().getHistory(
                message -> Platform.runLater(() -> {
                    roomId = message.getContent(ContentType.HISTORY_ROOM_ID);
                    createdAt = message.getContent(ContentType.HISTORY_CREATED_AT);
                    endedAt = message.getContent(ContentType.HISTORY_ENDED_AT);
                    resultType = message.getContent(ContentType.HISTORY_RESULT_TYPE);
                    balanceChanged = message.getContent(ContentType.HISTORY_BALANCE_CHANGED);

                    if (roomId.size() > 0) {
                        HBox header = new HBox(10);
                        header.getChildren().add(new Label("Room id"));
                        header.getChildren().add(new Label("Start time"));
                        header.getChildren().add(new Label("End time"));
                        header.getChildren().add(new Label("Result"));
                        header.getChildren().add(new Label("Balance"));
                        historyBox.getChildren().add(header);

                        for (int i = 0; i < roomId.size(); i++) {
                            HBox historyRow = new HBox(10);
                            historyRow.getChildren().add(new Label(String.valueOf(roomId.get(i).intValue())));
                            historyRow.getChildren().add(new Label(String.valueOf(createdAt.get(i))));
                            historyRow.getChildren().add(new Label(String.valueOf(endedAt.get(i))));
                            historyRow.getChildren().add(new Label(String.valueOf(resultType.get(i))));
                            historyRow.getChildren().add(new Label(String.valueOf(balanceChanged.get(i).intValue())));
                            historyBox.getChildren().add(historyRow);
                            historyPane.setContent(historyBox);
                        }

                        historyPane.setVisible(true);
                        noDataMessage.setVisible(false);
                    }
                }),
                error -> {
                    System.out.print("Error in get history");
                    historyPane.setVisible(false);
                    noDataMessage.setVisible(true);
                }
        );
    }
}
