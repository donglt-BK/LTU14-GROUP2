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
import javafx.scene.paint.Color;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.bk.olympia.config.Constant.HOME_SCREEN;

public class HistoryController extends ScreenService {
    public Label noDataMessage;
    private List<Double> roomId;
    private List<String> createdAt;
    private List<String> endedAt;
    private List<String> opponentList;
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
                    opponentList = message.getContent(ContentType.HISTORY_OPPONENT);
                    createdAt = message.getContent(ContentType.HISTORY_CREATED_AT);
                    endedAt = message.getContent(ContentType.HISTORY_ENDED_AT);
                    resultType = message.getContent(ContentType.HISTORY_RESULT_TYPE);
                    balanceChanged = message.getContent(ContentType.HISTORY_BALANCE_CHANGED);

                    if (roomId.size() > 0) {
                        HBox header = new HBox(10);
                        Label roomIdHeader = new Label("Room id");
                        roomIdHeader.setPrefWidth(80);
                        header.getChildren().add(roomIdHeader);

                        Label opponentHeader = new Label("Opponent");
                        opponentHeader.setPrefWidth(80);
                        header.getChildren().add(opponentHeader);

                        Label startTimeHeader = new Label("Start time");
                        startTimeHeader.setPrefWidth(100);
                        header.getChildren().add(startTimeHeader);

                        Label endTimeHeader = new Label("End time");
                        endTimeHeader.setPrefWidth(100);
                        header.getChildren().add(endTimeHeader);

                        Label resultHeader = new Label("Result");
                        resultHeader.setPrefWidth(60);
                        header.getChildren().add(resultHeader);

                        Label balanceHeader = new Label("Balance");
                        balanceHeader.setPrefWidth(60);
                        header.getChildren().add(balanceHeader);

                        historyBox.getChildren().add(header);

                        for (int i = 0; i < roomId.size(); i++) {
                            HBox historyRow = new HBox(10);
                            Label id = new Label(String.valueOf(roomId.get(i).intValue()));
                            id.setPrefWidth(80);
                            historyRow.getChildren().add(id);

                            Label opponent = new Label(opponentList.get(i));
                            opponent.setPrefWidth(80);
                            historyRow.getChildren().add(opponent);

                            Label createdTime = new Label(createdAt.get(i));
                            createdTime.setPrefWidth(100);
                            historyRow.getChildren().add(createdTime);

                            Label endTime = new Label(endedAt.get(i));
                            endTime.setPrefWidth(100);
                            historyRow.getChildren().add(endTime);

                            Label result = new Label(resultType.get(i));
                            result.setPrefWidth(60);
                            historyRow.getChildren().add(result);

                            Label balance = new Label(String.valueOf(balanceChanged.get(i).intValue()));
                            if (resultType.get(i).equals("LOSE")) {
                                balance.setTextFill(Color.RED);
                            } else if (resultType.get(i).equals("WIN")) {
                                balance.setTextFill(Color.GREEN);
                            }
                            balance.setPrefWidth(60);
                            historyRow.getChildren().add(balance);
                            addParticipant();
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
