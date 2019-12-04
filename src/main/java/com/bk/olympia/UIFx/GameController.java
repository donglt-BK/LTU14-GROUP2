package com.bk.olympia.UIFx;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.bk.olympia.config.Constant.HOME_SCREEN;
import static com.bk.olympia.config.Util.isNullOrEmpty;
import static com.bk.olympia.config.Util.parseIntOrZero;

public class GameController extends ScreenService {

    public Button cancelBtn, confirmBtn;
    public TextField answer_A_input, answer_B_input, answer_C_input, answer_D_input;
    public Button minus_A_10, plus_A_10, minus_A_100, plus_A_100, clear_A, all_in_A;
    public Button minus_B_10, plus_B_10, minus_B_100, plus_B_100, clear_B, all_in_B;
    public Button minus_C_10, plus_C_10, minus_C_100, plus_C_100, clear_C, all_in_C;
    public Button minus_D_10, plus_D_10, minus_D_100, plus_D_100, clear_D, all_in_D;
    public Label money;
    public TextField chatbox_input;
    public HBox row_A, row_B, row_C, row_D;

    private int totalMoney = parseIntOrZero("3000");

    static List<Label> messages = new ArrayList<>();
    public ScrollPane chatbox_scroll;

    private int index = 0;

    private final VBox chatBox = new VBox(5);

    public void initialize() {
        chatbox_scroll.setContent(chatBox);
        chatbox_input.setPromptText("Enter your message...");
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
        String invokerId = ((Button) event.getSource()).getId();

        String[] invokerIdSplit = invokerId.split("_");
        int invokerIdLength = invokerIdSplit.length;
        if (invokerIdLength > 2) {
            String firstSplit = invokerIdSplit[0],
                    secondSplit = invokerIdSplit[1],
                    lastSplit = invokerIdSplit[2];
            if (firstSplit.equals("all")) {
                switch (lastSplit) {
                    case "A":
                        answer_A_input.setText(String.valueOf(totalMoney));
                        answer_A_input.setDisable(true);
                        clear_A.setDisable(false);
                        answer_B_input.setText("");
                        answer_C_input.setText("");
                        answer_D_input.setText("");
                        answer_B_input.setDisable(true);
                        answer_C_input.setDisable(true);
                        answer_D_input.setDisable(true);
                        break;
                    case "B":
                        answer_B_input.setText(String.valueOf(totalMoney));
                        clear_B.setDisable(false);
                        answer_B_input.setDisable(true);
                        answer_A_input.setText("");
                        answer_C_input.setText("");
                        answer_D_input.setText("");
                        answer_A_input.setDisable(true);
                        answer_C_input.setDisable(true);
                        answer_D_input.setDisable(true);
                        break;
                    case "C":
                        answer_C_input.setText(String.valueOf(totalMoney));
                        answer_C_input.setDisable(true);
                        clear_C.setDisable(false);
                        answer_A_input.setText("");
                        answer_B_input.setText("");
                        answer_D_input.setText("");
                        answer_A_input.setDisable(true);
                        answer_B_input.setDisable(true);
                        answer_D_input.setDisable(true);
                        break;
                    case "D":
                        answer_D_input.setDisable(true);
                        clear_D.setDisable(false);
                        answer_D_input.setText(String.valueOf(totalMoney));
                        answer_A_input.setText("");
                        answer_B_input.setText("");
                        answer_C_input.setText("");
                        answer_A_input.setDisable(true);
                        answer_B_input.setDisable(true);
                        answer_C_input.setDisable(true);
                        break;
                }
            } else {
                int depositA = parseIntOrZero(answer_A_input.getText()),
                        depositB = parseIntOrZero(answer_B_input.getText()),
                        depositC = parseIntOrZero(answer_C_input.getText()),
                        depositD = parseIntOrZero(answer_D_input.getText()),
                        curMoney = totalMoney - (depositA + depositB + depositC + depositD);
                if (curMoney > 0) {
                    switch (firstSplit) {
                        case "plus":
                            switch (secondSplit) {

                            }
                            break;
                        case "minus":
                            break;
                    }
                }
            }
        } else if (invokerIdLength > 1) {
            String targetToClear = invokerIdSplit[invokerIdLength - 1];
            switch (targetToClear) {
                case "A":
                    answer_A_input.clear();
                    clear_A.setDisable(false);
                    break;
                case "B":
                    answer_B_input.clear();
                    clear_B.setDisable(false);
                    break;
                case "C":
                    answer_C_input.clear();
                    clear_C.setDisable(false);
                    break;
                case "D":
                    answer_D_input.clear();
                    clear_D.setDisable(false);
                    break;
            }
        } else {

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
            depositA = parseIntOrZero(answerAInput);
            depositB = parseIntOrZero(answerBInput);
            depositC = parseIntOrZero(answerCInput);
            depositD = parseIntOrZero(answerDInput);

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
//        String invokerId = ((Node) event.getSource()).getId();
//        int depositA = parseIntOrZero(answer_A_input.getText()),
//                depositB = parseIntOrZero(answer_B_input.getText()),
//                depositC = parseIntOrZero(answer_C_input.getText()),
//                depositD = parseIntOrZero(answer_D_input.getText()),
//                curMoney = totalMoney - (depositA + depositB + depositC + depositD);
//        if (curMoney>0){
//            if (invokerId.equals(answer_A_input.getId())) {
//
//            }
//        }
//
//        System.out.println("in");
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
}
