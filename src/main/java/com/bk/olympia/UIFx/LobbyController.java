package com.bk.olympia.UIFx;

import com.bk.olympia.model.UserSession;
import com.bk.olympia.request.socket.SocketService;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.List;

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
    public Hyperlink next_scene;

    static List<Label> messages = new ArrayList<>();

    private int index = 0;

    private final VBox chatBox = new VBox(5);

    public void initialize(){
        chatbox_scroll.setContent(chatBox);
        chatbox_input.setPromptText("Enter your message...");
    }

    public void onPressReady(ActionEvent event) {
        Paint yourReady = your_ready.getTextFill(),
                hisReady = your_opp_ready.getTextFill();
        int playerId = UserSession.getInstance().getUserId();
        if (isReady == false) {
            isReady = true;
            your_ready.setTextFill(Color.GREEN);
            your_ready.setText("Ready!");
            readyBtn.setText("I'm not ready");
            SocketService.getInstance().ready(String.valueOf(playerId),
                    response -> {
                        Boolean isAlpha = UserSession.getInstance().isAlpha();
                        if (hisReady.equals(Color.GREEN) && isAlpha) {
                            startBtn.setDisable(false);
                        } else {
                            startBtn.setDisable(true);
                        }
                    },
                    error -> {
                        showError("Ready Failed!", "Check your connection to server!");
                    }
            );
        } else {
            //TODO: send API unready
            isReady = false;
            your_ready.setTextFill(Color.RED);
            your_ready.setText("Not ready");
            readyBtn.setText("Ready!");
        }
    }

    public void onPressStart(ActionEvent event) {
        //TODO: add send game func
        changeScreen(event, GAME_SCREEN);
    }

    public void backToHome(ActionEvent event) {
        changeScreen(event, HOME_SCREEN);
    }

    public void sendMessage(ActionEvent event) {
        String message = chatbox_input.getText();
        if(!isNullOrEmpty(message)){
            messages.add(new Label("Khoa: "+message));
            if(index%2==0){
                messages.get(index).setAlignment(Pos.TOP_LEFT);
                System.out.println("1");
            }
            else{
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
