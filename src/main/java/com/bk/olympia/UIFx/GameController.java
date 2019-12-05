package com.bk.olympia.UIFx;

import com.bk.olympia.request.socket.SocketService;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.bk.olympia.config.Constant.HOME_SCREEN;
import static com.bk.olympia.config.Util.isNullOrEmpty;

public class GameController extends ScreenService {

    public Button cancelBtn, confirmBtn;
    public TextField answer_A_input, answer_B_input, answer_C_input, answer_D_input;
    public Button minus_A_10, plus_A_10, minus_A_100, plus_A_100, clear_A, all_in_A;
    public Button minus_B_10, plus_B_10, minus_B_100, plus_B_100, clear_B, all_in_B;
    public Button minus_C_10, plus_C_10, minus_C_100, plus_C_100, clear_C, all_in_C;
    public Button minus_D_10, plus_D_10, minus_D_100, plus_D_100, clear_D, all_in_D;
    public Label money;
    public TextField chatbox_input;

    static List<Label> messages = new ArrayList<>();
    public ScrollPane chatbox_scroll;

    private int index = 0;

    private final VBox chatBox = new VBox(5);

    public void initialize() {
        chatbox_scroll.setContent(chatBox);
        chatbox_input.setPromptText("Enter your message...");
        Thread t = new Thread(() ->
                SocketService.getInstance().roomChatSubscribe(
                        message -> {
                            //TODO: show the receive message
                        },
                        error -> {
                        }
                )
        );
        t.start();

    }

    public void onPressLeave(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm leave");
        alert.setHeaderText("You will lose your current credit. Do you really want to quit?");
        /*alert.getButtonTypes().clear();

        ButtonType confirm = new ButtonType("Confirm");
        ButtonType cancel = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(confirm, cancel);
        alert.showAndWait();*/

        Optional<ButtonType> option = alert.showAndWait();

        if (option.get() == ButtonType.OK) {
            //TODO: subtract current credit to balance
            changeScreen(event, HOME_SCREEN);
        }
    }

    public void onPressDepositBtn(ActionEvent event) {
        String eventInvokerId = ((Button) event.getSource()).getId();

        if (eventInvokerId.equals(minus_A_10.getId())) {

        }
        //add more to deposit input if possible
    }

    public void onPressConfirm(ActionEvent event) {
        String answerAInput = answer_A_input.getText(),
                answerBInput = answer_B_input.getText(),
                answerCInput = answer_C_input.getText(),
                answerDInput = answer_D_input.getText();

        int depositA, depositB, depositC, depositD;

        if (isNullOrEmpty(answerAInput) && isNullOrEmpty(answerBInput) && isNullOrEmpty(answerCInput) && isNullOrEmpty(answerDInput)) {
            showWarning("No answer!", "Please deposit at least one or you may lose all your current credit.");
            cancelBtn.setDisable(true);
        } else {
            depositA = Integer.parseInt(answerAInput.length() > 0 ? answerAInput : "0");
            depositB = Integer.parseInt(answerBInput.length() > 0 ? answerBInput : "0");
            depositC = Integer.parseInt(answerCInput.length() > 0 ? answerCInput : "0");
            depositD = Integer.parseInt(answerDInput.length() > 0 ? answerDInput : "0");

            cancelBtn.setDisable(false);
            confirmBtn.setDisable(true);
            //TODO: call api confirm answer
            //If confirm -> invisible cancelBtn
        }
    }

    public void onPressCancel(ActionEvent event) {
        answer_A_input.setText("");
        answer_B_input.setText("");
        answer_C_input.setText("");
        answer_D_input.setText("");
        cancelBtn.setDisable(true);
        confirmBtn.setDisable(false);
    }

    public void onDepositInputChange(KeyEvent event) {
        String invokerId = ((Node) event.getSource()).getId();
        if (invokerId.equals(answer_A_input.getId())) {
            answer_A_input.focusedProperty().addListener(
                    (observable, oldValue, newValue) -> System.out.println("Text changed from " + oldValue + " to " + newValue)
            );
        }
        System.out.println("in");
    }

    public void sendMessage(ActionEvent event) {
        System.out.println("send");
        String message = chatbox_input.getText();
        //TODO show sending message
        SocketService.getInstance().roomChat(message, error -> {
            //TODO handle unsend
        });
        /*if (!isNullOrEmpty(message)) {
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
        }*/
    }
}
