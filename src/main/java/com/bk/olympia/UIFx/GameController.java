package com.bk.olympia.UIFx;

import com.bk.olympia.model.UserSession;
import com.bk.olympia.request.socket.SocketService;
import com.bk.olympia.type.ContentType;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ObservableIntegerValue;
import javafx.beans.value.ObservableObjectValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.geometry.Pos;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.*;

import static com.bk.olympia.config.Constant.HOME_SCREEN;
import static com.bk.olympia.config.Util.isNullOrEmpty;
import static com.bk.olympia.config.Util.parseIntOrZero;

public class GameController extends ScreenService {

    public Button cancelBtn, confirmBtn;
    //public TextField answer_A_input, answer_B_input, answer_C_input, answer_D_input;
    public Button minus_A_10, plus_A_10, minus_A_100, plus_A_100, clear_A, all_in_A;
    public Button minus_B_10, plus_B_10, minus_B_100, plus_B_100, clear_B, all_in_B;
    public Button minus_C_10, plus_C_10, minus_C_100, plus_C_100, clear_C, all_in_C;
    public Button minus_D_10, plus_D_10, minus_D_100, plus_D_100, clear_D, all_in_D;
    public Label money;
    public TextField chatbox_input;
    public HBox row_A, row_B, row_C, row_D;

    public Spinner<Integer> answer_A_input = new Spinner<Integer>();
    public Spinner<Integer> answer_B_input = new Spinner<Integer>();
    public Spinner<Integer> answer_C_input = new Spinner<Integer>();
    public Spinner<Integer> answer_D_input = new Spinner<Integer>();
    public Label time_left;

    private SpinnerValueFactory valueFactoryA;
    private SpinnerValueFactory valueFactoryB;
    private SpinnerValueFactory valueFactoryC;
    private SpinnerValueFactory valueFactoryD;

    private int totalMoney, curMoney;

    static List<Label> messages = new ArrayList<>();
    public ScrollPane chatbox_scroll;

    private int index = 0;

    private final VBox chatBox = new VBox(5);

    public void initialize() {
        totalMoney = UserSession.getInstance().getCurBet();
        curMoney = totalMoney;
        money.setText(String.valueOf(curMoney));

        valueFactoryA = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, totalMoney, 0, 10);
        valueFactoryB = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, totalMoney, 0, 10);
        valueFactoryC = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, totalMoney, 0, 10);
        valueFactoryD = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, totalMoney, 0, 10);

        answer_A_input.setValueFactory(valueFactoryA);
        answer_A_input.getStyleClass().add(Spinner.STYLE_CLASS_ARROWS_ON_RIGHT_HORIZONTAL);
        answer_B_input.setValueFactory(valueFactoryB);
        answer_B_input.getStyleClass().add(Spinner.STYLE_CLASS_ARROWS_ON_RIGHT_HORIZONTAL);
        answer_C_input.setValueFactory(valueFactoryC);
        answer_C_input.getStyleClass().add(Spinner.STYLE_CLASS_ARROWS_ON_RIGHT_HORIZONTAL);
        answer_D_input.setValueFactory(valueFactoryD);
        answer_D_input.getStyleClass().add(Spinner.STYLE_CLASS_ARROWS_ON_RIGHT_HORIZONTAL);

//        answer_A_input.getEditor().textProperty().addListener((arg0, oldValue, newValue) -> {
//            int moneyChange = curMoney - parseIntOrZero(newValue);
//            if (moneyChange < 0) {
//                answer_A_input.getEditor().setText(String.valueOf(oldValue));
//            } else {
//                curMoney = curMoney - parseIntOrZero(newValue) + parseIntOrZero(oldValue);
//                money.setText(String.valueOf(moneyChange));
//            }
//        });
//        answer_B_input.getEditor().textProperty().addListener((arg0, oldValue, newValue) -> {
//            int moneyChange = curMoney - parseIntOrZero(newValue);
//            if (moneyChange < 0) {
//                answer_B_input.getEditor().setText(String.valueOf(oldValue));
//            } else {
//                curMoney = curMoney - parseIntOrZero(newValue) + parseIntOrZero(oldValue);
//                money.setText(String.valueOf(moneyChange));
//            }
//        });
//        answer_C_input.getEditor().textProperty().addListener((arg0, oldValue, newValue) -> {
//            int moneyChange = curMoney - parseIntOrZero(newValue);
//            if (moneyChange < 0) {
//                answer_C_input.getEditor().setText(String.valueOf(oldValue));
//            } else {
//                curMoney = curMoney - parseIntOrZero(newValue) + parseIntOrZero(oldValue);
//                money.setText(String.valueOf(moneyChange));
//            }
//        });
//        answer_D_input.getEditor().textProperty().addListener((arg0, oldValue, newValue) -> {
//            int moneyChange = curMoney - parseIntOrZero(newValue);
//            if (moneyChange < 0) {
//                answer_D_input.getEditor().setText(String.valueOf(oldValue));
//            }
//            else {
//                curMoney = curMoney - parseIntOrZero(newValue) + parseIntOrZero(oldValue);
//                money.setText(String.valueOf(moneyChange));
//            }
//        });

        chatbox_scroll.setContent(chatBox);
        chatbox_input.setPromptText("Enter your message...");
        Thread t = new Thread(() ->
                SocketService.getInstance().roomChatSubscribe(
                        message -> Platform.runLater(() -> {
                            System.out.println("receiving message...");
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
                            m.setPrefWidth(chatBox.getPrefWidth());
                            m.setTextFill(Color.RED);
                            messages.add(m);
                            chatBox.getChildren().add(messages.get(index));
                            index++;
                        }

                )
        );
        t.start();

        setTimer();
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

    /*
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
                        answer_A_input.setDisable(false);
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
                        answer_B_input.setDisable(true);
                        clear_B.setDisable(false);
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
                            int tmpDeposit = parseIntOrZero(lastSplit);
                            if ((lastSplit.equals("100") && curMoney - tmpDeposit > 0) || (lastSplit.equals("10") && curMoney - tmpDeposit > 0)) {
                                switch (secondSplit) {
                                    case "A":
                                        depositA += tmpDeposit;
                                        answer_A_input.setText(String.valueOf(depositA));
                                        clear_A.setDisable(false);
                                        if (depositA >= 100) {
                                            minus_A_10.setDisable(false);
                                            minus_A_100.setDisable(false);
                                        } else {
                                            minus_A_10.setDisable(false);
                                            minus_A_100.setDisable(true);
                                        }
                                        break;
                                    case "B":
                                        depositB += tmpDeposit;
                                        answer_B_input.setText(String.valueOf(depositB));
                                        clear_B.setDisable(false);
                                        if (depositB >= 100) {
                                            minus_B_10.setDisable(false);
                                            minus_B_100.setDisable(false);
                                        } else {
                                            minus_B_10.setDisable(false);
                                            minus_B_100.setDisable(true);
                                        }
                                        break;
                                    case "C":
                                        depositC += tmpDeposit;
                                        answer_C_input.setText(String.valueOf(depositC));
                                        clear_C.setDisable(false);
                                        if (depositC >= 100) {
                                            minus_C_10.setDisable(false);
                                            minus_C_100.setDisable(false);
                                        } else {
                                            minus_C_10.setDisable(false);
                                            minus_C_100.setDisable(true);
                                        }
                                        break;
                                    case "D":
                                        depositD += tmpDeposit;
                                        answer_D_input.setText(String.valueOf(depositD));
                                        clear_D.setDisable(false);
                                        if (depositD >= 100) {
                                            minus_D_10.setDisable(false);
                                            minus_D_100.setDisable(false);
                                        } else {
                                            minus_D_10.setDisable(false);
                                            minus_D_100.setDisable(true);
                                        }
                                        break;
                                }
                            }

                            break;
                        case "minus":
                            switch (secondSplit) {
                                case "A":
                                    break;
                                case "B":
                                    break;
                                case "C":
                                    break;
                                case "D":
                                    break;
                            }
                            break;
                    }
                }
            }
        } else if (invokerIdLength > 1) {
            String targetToClear = invokerIdSplit[invokerIdLength - 1];
            int depositA = parseIntOrZero(answer_A_input.getText()),
                    depositB = parseIntOrZero(answer_B_input.getText()),
                    depositC = parseIntOrZero(answer_C_input.getText()),
                    depositD = parseIntOrZero(answer_D_input.getText()),
                    curMoney = totalMoney - (depositA + depositB + depositC + depositD);

            switch (targetToClear) {
                case "A":
                    answer_A_input.clear();
                    answer_A_input.setDisable(false);
                    minus_A_100.setDisable(true);
                    minus_A_10.setDisable(true);
                    clear_A.setDisable(true);
                    break;
                case "B":
                    answer_B_input.clear();
                    answer_B_input.setDisable(false);
                    minus_B_100.setDisable(true);
                    minus_B_10.setDisable(true);
                    clear_B.setDisable(true);
                    break;
                case "C":
                    answer_C_input.clear();
                    answer_C_input.setDisable(false);
                    minus_C_100.setDisable(true);
                    minus_C_10.setDisable(true);
                    clear_C.setDisable(true);
                    break;
                case "D":
                    answer_D_input.clear();
                    answer_D_input.setDisable(false);
                    minus_D_100.setDisable(true);
                    minus_D_10.setDisable(true);
                    clear_D.setDisable(true);
                    break;
            }
        } else {

        }
        //add more to deposit input if possible
    }
    */

    public void onPressConfirm(ActionEvent event) {
        String answerAInput = answer_A_input.getEditor().getText(),
                answerBInput = answer_B_input.getEditor().getText(),
                answerCInput = answer_C_input.getEditor().getText(),
                answerDInput = answer_D_input.getEditor().getText();
        int depositA, depositB, depositC, depositD;
        System.out.println("A: " + answerAInput);
        System.out.println("B: " + answerBInput);
        System.out.println("C: " + answerCInput);
        System.out.println("D: " + answerDInput);

        depositA = parseIntOrZero(answerAInput);
        depositB = parseIntOrZero(answerBInput);
        depositC = parseIntOrZero(answerCInput);
        depositD = parseIntOrZero(answerDInput);

        if (depositA + depositB + depositC + depositD == 0) {
            showWarning("No answer!", "Please deposit into at least one answer or you may lose all your current credit.");
//            cancelBtn.setDisable(true);
        } else if (depositA + depositB + depositC + depositD > curMoney) {
            showWarning("Not enough money!", "You currently don't have enough money to deposit this amount of money.");
//            cancelBtn.setDisable(true);
        } else {
//            cancelBtn.setDisable(false);
            isSubmit = true;
            confirmBtn.setDisable(true);
            //TODO: call api confirm answer
            //If confirm -> invisible cancelBtn
        }
    }

//    public void onPressCancel(ActionEvent event) {
//        answer_A_input.getValueFactory().setValue(0);
//        answer_A_input.getEditor().setText("0");
//        answer_B_input.getValueFactory().setValue(0);
//        answer_B_input.getEditor().setText("0");
//        answer_C_input.getValueFactory().setValue(0);
//        answer_C_input.getEditor().setText("0");
//        answer_D_input.getValueFactory().setValue(0);
//        answer_D_input.getEditor().setText("0");
//        cancelBtn.setDisable(true);
//        confirmBtn.setDisable(false);
//    }

    public void sendMessage(ActionEvent event) {
        String message = chatbox_input.getText();

        if (!isNullOrEmpty(message)) {
            Label m = new Label(UserSession.getInstance().getName() + ": " + message);
            m.setTextFill(Color.GREEN);
            messages.add(m);
            chatBox.getChildren().add(m);
            chatbox_input.setText("");
            index++;
            SocketService.getInstance().roomChat(message, error -> {
                Label err = new Label("Failed to retrieve message from server. Trying again...");
                err.setTextFill(Color.RED);
                messages.add(err);
                chatBox.getChildren().add(messages.get(index));
                index++;
            });

        }
    }

    public void onPressClear(ActionEvent event) {
        Button button = (Button) event.getSource();
        if (button.equals(clear_A)) {
            answer_A_input.getValueFactory().setValue(0);
            answer_A_input.getEditor().setText("0");
        } else if (button.equals(clear_B)) {
            answer_B_input.getValueFactory().setValue(0);
            answer_B_input.getEditor().setText("0");
        } else if (button.equals(clear_C)) {
            answer_C_input.getValueFactory().setValue(0);
            answer_C_input.getEditor().setText("0");
        } else if (button.equals(clear_D)) {
            answer_D_input.getValueFactory().setValue(0);
            answer_D_input.getEditor().setText("0");
        }
    }


    public void onPressDepositAll(ActionEvent event) {
        Button button = (Button) event.getSource();
        int A_input = parseIntOrZero(answer_A_input.getEditor().getText());
        int B_input = parseIntOrZero(answer_B_input.getEditor().getText());
        int C_input = parseIntOrZero(answer_C_input.getEditor().getText());
        int D_input = parseIntOrZero(answer_D_input.getEditor().getText());

        int value;

        if (button.equals(all_in_A)) {
            value = curMoney - B_input - C_input - D_input;
            answer_A_input.getValueFactory().setValue(value);
            answer_A_input.getEditor().setText(Integer.toString(value));
        } else if (button.equals(all_in_B)) {
            value = curMoney - A_input - C_input - D_input;
            answer_B_input.getValueFactory().setValue(value);
            answer_B_input.getEditor().setText(Integer.toString(value));
        } else if (button.equals(all_in_C)) {
            value = curMoney - A_input - B_input - D_input;
            answer_C_input.getValueFactory().setValue(value);
            answer_C_input.getEditor().setText(Integer.toString(value));
        } else if (button.equals(all_in_D)) {
            value = curMoney - A_input - B_input - C_input;
            answer_D_input.getValueFactory().setValue(value);
            answer_D_input.getEditor().setText(Integer.toString(value));
        }
    }

    int interval = 5;
    Boolean isSubmit = false;
    public void setTimer() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (interval > 0) {
                    Platform.setImplicitExit(false);
                    Platform.runLater(() -> time_left.setText(String.valueOf(interval)));
                    System.out.println(interval);
                    --interval;
                } else{
                    Platform.setImplicitExit(false);
                    Platform.runLater(() -> {
                        if (!isSubmit){
                            //TODO: call submit answer to server
                        }
                        confirmBtn.setText("Timeout");
                        confirmBtn.setTextFill(Color.RED);
                        confirmBtn.setDisable(true);
                    });
                    timer.cancel();
                }
            }
        }, 1000, 1000);
    }
}
