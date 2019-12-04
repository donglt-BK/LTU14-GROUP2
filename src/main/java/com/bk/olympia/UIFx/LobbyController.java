package com.bk.olympia.UIFx;

import com.bk.olympia.model.UserSession;
import com.bk.olympia.request.socket.SocketService;
import com.bk.olympia.type.ContentType;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.List;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.bk.olympia.config.Constant.*;
import static com.bk.olympia.config.Util.isNullOrEmpty;

public class LobbyController extends ScreenService {
    public Label your_ready, your_opp_ready;
    public Button readyBtn, startBtn;
    private static boolean isReady = false;
    public Hyperlink back_home;
    public Button chat_send;
    public TextField chatbox_input;
    public ScrollPane chatbox_scroll;
    public Hyperlink next_scene;
    public ImageView opponentImg;
    public Label opponentLabel;
    private static List<Label> messages = new ArrayList<>();

    private int index = 0;

    private final VBox chatBox = new VBox(5);

    @FXML
    public void initialize() {
        chatbox_scroll.setContent(chatBox);
        chatbox_input.setPromptText("Enter your message...");

        Alert alert = new Alert(Alert.AlertType.NONE);
        AtomicBoolean found = new AtomicBoolean(false);
        SocketService.getInstance().findGame(
                success -> Platform.runLater(() -> {
                    if (success.getContent().containsKey(ContentType.STATUS)) {
                        //join queue success
                        System.out.println("Finding another player...");
                        alert.setTitle("Find random player");
                        alert.setHeaderText("Finding another player, please wait...");
                        alert.getButtonTypes().clear();

                        ButtonType cancel = new ButtonType("Cancel search");
                        alert.getButtonTypes().setAll(cancel);
                        if (!found.get()) alert.show();
                    } else {
                        found.set(true);
                        alert.hide();
                        //joined lobby
                        String lobbyId = String.valueOf((Double) success.getContent(ContentType.LOBBY_ID));
                        lobbyId = lobbyId.substring(0, lobbyId.length() - 2);
                        String lobbyName = success.getContent(ContentType.LOBBY_NAME);
                        List<String> lobbyParticipant = success.getContent(ContentType.LOBBY_PARTICIPANT);

                        if (!lobbyParticipant.isEmpty()) {
                            if (lobbyParticipant.get(0).equals(UserSession.getInstance().getName())) {
                                if (lobbyParticipant.size() == 2) {
                                    UserSession.getInstance().setLobby(true, lobbyId, lobbyName, lobbyParticipant.get(1));
                                } else {
                                    UserSession.getInstance().setLobby(true, lobbyId, lobbyName, null);
                                }
                            } else {
                                if (lobbyParticipant.size() == 2) {
                                    UserSession.getInstance().setLobby(false, lobbyId, lobbyName, lobbyParticipant.get(0));
                                }
                            }
                            if (lobbyParticipant.size() == 2) {
                                opponentImg.setVisible(true);
                                opponentLabel.setText(lobbyParticipant.get(1));
                                opponentLabel.setVisible(true);
                            } else {
                                opponentImg.setVisible(false);
                                opponentLabel.setVisible(false);
                            }
                        } else {
                            showError("No lobby participant!", "Ask your admin about this feature~!");
                        }
                    }
                }),
                errorMessage -> System.out.println("Login error: " + errorMessage.getErrorType())
        );
    }

    public void onPressReady(ActionEvent event) {
        Paint yourReady = your_ready.getTextFill(),
                hisReady = your_opp_ready.getTextFill();
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
        SocketService.getInstance().leave(UserSession.getInstance().getCurrentLobbyId(),
                success -> {},
                error -> showError("Failed!", "Check your connection to server!")
        );
        UserSession.getInstance().resetLobby();
        changeScreen(event, HOME_SCREEN);
    }

    public void sendMessage(ActionEvent event) {
        String message = chatbox_input.getText();
        if (!isNullOrEmpty(message)) {
            messages.add(new Label("Khoa: " + message));
            if (index % 2 == 0) {
                messages.get(index).setAlignment(Pos.TOP_LEFT);
                System.out.println("1");
            } else {
                messages.get(index).setAlignment(Pos.TOP_RIGHT);
                System.out.println("2");
            }
            chatBox.getChildren().add(messages.get(index));
            chatbox_input.setText("");
            index++;
        }
    }

    public void nextScene(ActionEvent event) {
        changeScreen(event, GAME_SCREEN);
    }


}
