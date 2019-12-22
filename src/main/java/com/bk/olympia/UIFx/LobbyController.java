package com.bk.olympia.UIFx;

import com.bk.olympia.model.UserSession;
import com.bk.olympia.request.socket.SocketService;
import com.bk.olympia.type.ContentType;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.bk.olympia.config.Constant.GAME_SCREEN;
import static com.bk.olympia.config.Constant.HOME_SCREEN;
import static com.bk.olympia.config.Util.isNullOrEmpty;

public class LobbyController extends ScreenService {
    public Label your_ready, your_opp_ready;
    public Button readyBtn, startBtn;
    private static boolean isReady = false;
    public Hyperlink back_home;
    public Button chat_send;
    public TextField chatbox_input;
    public ScrollPane chatbox_scroll;
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
                        List<String> lobbyParticipant = success.getContent(ContentType.LOBBY_PARTICIPANT);
                        if (!found.get()) {
                            if (lobbyParticipant.get(0).equals(UserSession.getInstance().getName())) {
                                your_ready.setText("Host");
                                your_ready.setTextFill(Color.GREEN);
                                readyBtn.setVisible(false);
                                your_opp_ready.setText("Not ready");
                                your_opp_ready.setTextFill(Color.RED);
                                startBtn.setVisible(true);
                            } else {
                                your_ready.setText("Not ready");
                                readyBtn.setText("Ready!");
                                readyBtn.setVisible(true);
                                your_opp_ready.setText("Host");
                                your_opp_ready.setTextFill(Color.GREEN);
                                your_opp_ready.setVisible(true);
                                startBtn.setVisible(false);
                            }
                            found.set(true);
                            alert.hide();
                        }
                        //joined lobby
                        String lobbyId = String.valueOf((Double) success.getContent(ContentType.LOBBY_ID));
                        lobbyId = lobbyId.substring(0, lobbyId.length() - 2);
                        String lobbyName = success.getContent(ContentType.LOBBY_NAME);

                        //receive message from another player
                        if (UserSession.getInstance().getCurrentLobbyId() == -1) {
                            SocketService.getInstance().lobbyChatSubscribe(lobbyId,
                                    message -> Platform.runLater(() -> {
                                        if (message.getSender() != UserSession.getInstance().getUserId()) {
                                            String m = message.getContent(ContentType.CHAT);
                                            Label opponentMessage = new Label(UserSession.getInstance().getLobbyParticipant() + ": " + m);
                                            opponentMessage.setTextFill(Color.BLUE);
                                            messages.add(opponentMessage);
                                            chatBox.getChildren().add(messages.get(index));
                                            index++;
                                        }
                                    }),
                                    error -> {
                                        Label m = new Label("Failed to retrieve message from server. Trying again...");
                                        m.setTextFill(Color.RED);
                                        messages.add(m);
                                        chatBox.getChildren().add(messages.get(index));
                                        index++;
                                    }
                            );
                        }

                        //listen ready
                        SocketService.getInstance().readySubscribe(
                                response -> Platform.runLater(() -> {
                                    boolean isAlpha = UserSession.getInstance().isAlpha();
                                    if (!isAlpha) return;
                                    if (response.getContent(ContentType.READY)) {
                                        your_opp_ready.setText("Ready");
                                        your_opp_ready.setTextFill(Color.GREEN);
                                        startBtn.setDisable(false);
                                    } else {
                                        your_opp_ready.setText("Not ready");
                                        your_opp_ready.setTextFill(Color.RED);
                                        startBtn.setDisable(true);
                                    }
                                }),
                                error -> Platform.runLater(() -> {
                                    showError("Failed!", "Check your connection to server!");
                                })
                        );

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
                                if (UserSession.getInstance().isAlpha()) {
                                    opponentLabel.setText(lobbyParticipant.get(1));
                                } else {
                                    opponentLabel.setText(lobbyParticipant.get(0));
                                }
                                opponentLabel.setVisible(true);
                                your_opp_ready.setVisible(true);
                            } else {
                                opponentImg.setVisible(false);
                                opponentLabel.setVisible(false);
                                your_opp_ready.setVisible(false);
                            }
                        } else {
                            showError("No lobby participant!", "Ask your admin about this feature~!");
                        }
                    }
                }),
                errorMessage -> System.out.println("Login error: " + errorMessage.getErrorType())
        );
        SocketService.getInstance().subscribeLeaveLobby(
                success -> Platform.runLater(() -> {
                    if (!UserSession.getInstance().isAlpha()) {
                        your_ready.setText("Host");
                        your_ready.setTextFill(Color.GREEN);
                        readyBtn.setVisible(false);
                        startBtn.setVisible(true);
                        startBtn.setDisable(true);
                    }
                    opponentImg.setVisible(false);
                    opponentLabel.setVisible(false);

                    your_opp_ready.setVisible(false);
                    your_opp_ready.setText("Not ready");
                    your_opp_ready.setTextFill(Color.RED);
                }),
                errorMessage -> System.out.println("Login error: " + errorMessage.getErrorType())
        );
    }

    public void onPressReady(ActionEvent event) {
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
        Thread t = new Thread(() -> Platform.runLater(() -> {
            SocketService.getInstance().sendReady();
            SocketService.getInstance().subscribesStart(
                    success -> Platform.runLater(() -> {
                        UserSession.getInstance().setRoomId(success.getContent(ContentType.ROOM_ID));
                        System.out.println("Starting");
                        changeScreen(event, GAME_SCREEN);
                    }),
                    error -> Platform.runLater(() -> {
                        showError("Failed!", "Check your connection to server!");
                    })
            );
        }));
        t.start();
    }

    public void onPressStart(ActionEvent event) {
        SocketService.getInstance().start(
                success -> Platform.runLater(() -> {
                    UserSession.getInstance().setRoomId(success.getContent(ContentType.ROOM_ID));
//                    UserSession.getInstance().setCurBet(success.getContent());
                    System.out.println("Starting");
                    changeScreen(event, GAME_SCREEN);
                }),
                error -> {
                    showError("Failed!", "Check your connection to server!");
                }
        );
    }

    public void backToHome(ActionEvent event) {
        SocketService.getInstance().leaveLobby(UserSession.getInstance().getCurrentLobbyId());
        UserSession.getInstance().resetLobby();
        changeScreen(event, HOME_SCREEN);
    }

    public void sendMessage(ActionEvent event) {
        String message = chatbox_input.getText();

        if (!isNullOrEmpty(message)) {
            Label m = new Label(UserSession.getInstance().getName() + ": " + message);
            m.setTextFill(Color.GREEN);
            messages.add(m);
            chatBox.getChildren().add(messages.get(index));
            chatbox_input.setText("");
            index++;
            SocketService.getInstance().lobbyChat(message, error -> {
                Label err = new Label("Failed to send message to server. Trying again...");
                err.setTextFill(Color.RED);
                messages.add(err);
                chatBox.getChildren().add(messages.get(index));
                index++;
            });
        }
    }

    public void nextScene(ActionEvent event) {
        changeScreen(event, GAME_SCREEN);
    }


}
