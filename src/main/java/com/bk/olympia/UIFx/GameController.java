package com.bk.olympia.UIFx;

import com.bk.olympia.model.UserSession;
import com.bk.olympia.request.socket.SocketService;
import com.bk.olympia.type.ContentType;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.*;

import static com.bk.olympia.config.Constant.HOME_SCREEN;
import static com.bk.olympia.config.Util.isNullOrEmpty;
import static com.bk.olympia.config.Util.parseIntOrZero;
import static com.bk.olympia.type.ContentType.ANSWER;
import static com.bk.olympia.type.ContentType.QUESTION;

public class GameController extends ScreenService {
    private static final int WIN = 1, DRAW = 2, LOSE = 3;

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
    public Label time_label;
    public Label question;
    public Label answerA;
    public Label answerB;
    public Label answerC;
    public Label answerD;
    public Rectangle answerBackgroundA;
    public Rectangle answerBackgroundB;
    public Rectangle answerBackgroundC;
    public Rectangle answerBackgroundD;
    public Rectangle blocker;
    public Button backToHome;

    private int currentMoney;

    private int interval;
    private boolean isSubmit;
    private boolean isAnswerCountdown;

    private static List<Label> messages = new ArrayList<>();
    public ScrollPane chatbox_scroll;

    private int index = 0;

    private final VBox chatBox = new VBox(5);

    private int questionId = -1;

    private String questionStr;
    private List<String> answer;
    private boolean isWait = true;

    private int receivedAnswer = 0;

    private Double moneyLeft;
    private Double answerIndex;

    private boolean gameOver = false;

    private int status;
    private int moneyChange;

    private Double opponentMoneyLeft;

    private boolean surrender = false;
    public void initialize() {
        int totalMoney = UserSession.getInstance().getCurBet();
        currentMoney = totalMoney;

        money.setText(String.valueOf(currentMoney));

        SpinnerValueFactory<Integer> valueA = new IntegerSpinnerValueFactory(0, totalMoney, 0, 10);
        SpinnerValueFactory<Integer> valueB = new IntegerSpinnerValueFactory(0, totalMoney, 0, 10);
        SpinnerValueFactory<Integer> valueC = new IntegerSpinnerValueFactory(0, totalMoney, 0, 10);
        SpinnerValueFactory<Integer> valueD = new IntegerSpinnerValueFactory(0, totalMoney, 0, 10);

        answer_A_input.setValueFactory(valueA);
        answer_A_input.getStyleClass().add(Spinner.STYLE_CLASS_ARROWS_ON_RIGHT_HORIZONTAL);
        answer_B_input.setValueFactory(valueB);
        answer_B_input.getStyleClass().add(Spinner.STYLE_CLASS_ARROWS_ON_RIGHT_HORIZONTAL);
        answer_C_input.setValueFactory(valueC);
        answer_C_input.getStyleClass().add(Spinner.STYLE_CLASS_ARROWS_ON_RIGHT_HORIZONTAL);
        answer_D_input.setValueFactory(valueD);
        answer_D_input.getStyleClass().add(Spinner.STYLE_CLASS_ARROWS_ON_RIGHT_HORIZONTAL);

        chatbox_scroll.setContent(chatBox);
        chatbox_input.setPromptText("Enter your message...");

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (surrender) return;
                if (interval > 0) {
                    Platform.setImplicitExit(false);
                    Platform.runLater(() -> time_left.setText(interval + "s"));
                    --interval;
                } else {
                    if (!isWait) {
                        if (isAnswerCountdown) {
                            Platform.setImplicitExit(false);
                            Platform.runLater(() -> {
                                time_left.setText("Timeout");
                                confirmBtn.setDisable(true);
                            });
                            if (!isSubmit) {
                                submitAnswer();
                            }
                            isWait = true;
                        } else {
                            Platform.runLater(() -> {
                                blocker.setVisible(false);
                                question.setText(questionStr);
                                answerA.setText(answer.get(0));
                                answerB.setText(answer.get(1));
                                answerC.setText(answer.get(2));
                                answerD.setText(answer.get(3));
                                answerBackgroundA.setFill(Color.rgb(252, 252, 252));
                                answerBackgroundB.setFill(Color.rgb(252, 252, 252));
                                answerBackgroundC.setFill(Color.rgb(252, 252, 252));
                                answerBackgroundD.setFill(Color.rgb(252, 252, 252));
                                time_label.setText("Time left: ");
                                time_left.setText("20s");
                                setValue(0, 0, 0, 0);

                                confirmBtn.setDisable(false);
                            });
                            interval = 20;
                            isAnswerCountdown = true;
                        }
                    }
                }
            }
        }, 1000, 1000);

        Thread t = new Thread(() -> {
            //chat
            SocketService.getInstance().roomChatSubscribe(
                    message -> Platform.runLater(() -> {
                        if (message.getSender() != UserSession.getInstance().getUserId()) {
                            String m = message.getContent(ContentType.CHAT);
                            addMessage(m, false, Color.BLUE, false);
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
            );

            //receive surrender
            SocketService.getInstance().subscribeSurrender(message -> Platform.runLater(() -> {
                        if (message.getSender() != UserSession.getInstance().getUserId()) {
                            addMessage("Your oppenent surrendered", true, Color.GREEN, false);
                            addMessage("YOU WINNNN.", true, Color.GREEN, false);
                            UserSession.getInstance().addBalance(2000);
                            backToHome.setVisible(true);
                        }
                    }),
                    error -> {
                        Label m = new Label("Failed to retrieve message from server. Trying again...");
                        m.setPrefWidth(chatBox.getPrefWidth());
                        m.setTextFill(Color.RED);
                        messages.add(m);
                        chatBox.getChildren().add(messages.get(index));
                        index++;
                    });

            //receive question
            SocketService.getInstance().subscribeQuestion(
                    message -> Platform.runLater(() -> {
                        Double questionId = message.getContent(ContentType.QUESTION_ID);
                        String question = message.getContent(QUESTION);
                        List<String> answer = message.getContent(ANSWER);

                        this.questionId = questionId.intValue();
                        questionStr = question;
                        this.answer = answer;
                        setTimer();
                    }),
                    error -> {
                        Label m = new Label("Failed to retrieve message from server. Trying again...");
                        m.setPrefWidth(chatBox.getPrefWidth());
                        m.setTextFill(Color.RED);
                        messages.add(m);
                        chatBox.getChildren().add(messages.get(index));
                        index++;
                    });

            //receive answer
            SocketService.getInstance().receiveAnswer(
                    message -> Platform.runLater(() -> {
                        if (message.getSender() == UserSession.getInstance().getUserId()) {
                            answerIndex = message.getContent(ContentType.ANSWER);
                            moneyLeft = message.getContent(ContentType.MONEY_PLACED);
                            if (!gameOver) gameOver = moneyLeft.intValue() == 0;
                        } else {
                            opponentMoneyLeft = message.getContent(ContentType.MONEY_PLACED);
                            if (!gameOver) gameOver = opponentMoneyLeft.intValue() == 0;
                        }
                        receivedAnswer++;
                        if (receivedAnswer == 2) {
                            receivedAnswer = 0;

                            answerBackgroundA.setFill(Color.rgb(255, 142, 142));
                            answerBackgroundB.setFill(Color.rgb(255, 142, 142));
                            answerBackgroundC.setFill(Color.rgb(255, 142, 142));
                            answerBackgroundD.setFill(Color.rgb(255, 142, 142));
                            switch (answerIndex.intValue()) {
                                case 0:
                                    answerBackgroundA.setFill(Color.rgb(151, 255, 128));
                                    break;
                                case 1:
                                    answerBackgroundB.setFill(Color.rgb(151, 255, 128));
                                    break;
                                case 2:
                                    answerBackgroundC.setFill(Color.rgb(151, 255, 128));
                                    break;
                                case 3:
                                    answerBackgroundD.setFill(Color.rgb(151, 255, 128));
                                    break;
                            }

                            currentMoney = moneyLeft.intValue();
                            money.setText(String.valueOf(currentMoney));

                            if (moneyLeft.intValue() == 0) {
                                addMessage("You lose everything.", true, Color.RED, false);
                            } else {
                                addMessage("You passed with " + moneyLeft.intValue() + " left.", true, Color.GREEN, false);
                            }
                            if (opponentMoneyLeft.intValue() == 0) {
                                addMessage("Your opponent lose everything.", true, Color.BLUE, false);
                            } else {
                                addMessage("Your opponent passed with " + opponentMoneyLeft.intValue() + " left.", true, Color.BLUE, false);
                            }
                            if (!gameOver) {
                                getQuestion();
                            } else {
                                showStatus();
                            }
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
            );
            //game over
            SocketService.getInstance().gameOver(
                    message -> Platform.runLater(() -> {
                        System.out.println("GAME OVER");
                        Double betVal = message.getContent(ContentType.BET_VALUE);
                        List<Double> winner = message.getContent(ContentType.WINNER);
                        System.out.println(betVal);
                        moneyChange = betVal.intValue();
                        if (winner.size() == 2) {
                            status = DRAW;
                        } else {
                            if (winner.get(0).intValue() == UserSession.getInstance().getUserId()) {
                                status = WIN;
                            } else {
                                status = LOSE;
                            }
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
            );

            //delayer
            long time = System.currentTimeMillis();
            while (System.currentTimeMillis() - time < 1000) {
            }
            getQuestion();
        });
        t.start();
    }

    private void showStatus() {
        if (status == DRAW) {
            addMessage("IT'S A DRAW.", true, Color.GREEN, false);
        } else if (status == WIN) {
            addMessage("YOU WINNNN.", true, Color.GREEN, false);
            UserSession.getInstance().addBalance(moneyChange);
        } else if (status == LOSE) {
            addMessage("YOU LOSE :(", true, Color.RED, false);
            UserSession.getInstance().addBalance(-moneyChange);
        }
        backToHome.setVisible(true);
    }

    public void submitAnswer() {
        blocker.setVisible(true);
        if (gameOver) return;

        String answerAInput = answer_A_input.getEditor().getText(),
                answerBInput = answer_B_input.getEditor().getText(),
                answerCInput = answer_C_input.getEditor().getText(),
                answerDInput = answer_D_input.getEditor().getText();
        int depositA, depositB, depositC, depositD;
        depositA = parseIntOrZero(answerAInput);
        depositB = parseIntOrZero(answerBInput);
        depositC = parseIntOrZero(answerCInput);
        depositD = parseIntOrZero(answerDInput);

        if (depositA + depositB + depositC + depositD != currentMoney) {
            if (depositA + depositB + depositC + depositD > currentMoney) {
                int extended = (depositA + depositB + depositC + depositD) - currentMoney;
                int count = 0;
                if (depositA != 0) count++;
                if (depositB != 0) count++;
                if (depositC != 0) count++;
                if (depositD != 0) count++;
                int each = (extended - (extended % count)) / count;
                if (depositA != 0) depositA -= each;
                if (depositB != 0) depositB -= each;
                if (depositC != 0) depositC -= each;
                if (depositD != 0) depositD -= each;

                if (depositA != 0) {
                    depositA -= extended - each * count;
                } else if (depositB != 0) {
                    depositB -= extended - each * count;
                } else if (depositC != 0) {
                    depositC -= extended - each * count;
                } else {
                    depositD -= extended - each * count;
                }
            } else if (depositA + depositB + depositC + depositD < currentMoney) {
                int leftover = currentMoney - (depositA + depositB + depositC + depositD);
                int each = (leftover - (leftover % 4)) / 4;
                depositA += each;
                depositB += each;
                depositC += each;
                depositD += each;

                depositA += leftover - each * 4;
            }
            setValue(depositA, depositB, depositC, depositD);
        }
        isSubmit = true;
        confirmBtn.setDisable(true);
        int[] placement = {depositA, depositB, depositC, depositD};
        Thread t = new Thread(() -> SocketService.getInstance().submit(questionId, placement, error -> {
            Label m = new Label("Failed to retrieve message from server. Trying again...");
            m.setPrefWidth(chatBox.getPrefWidth());
            m.setTextFill(Color.RED);
            messages.add(m);
            chatBox.getChildren().add(messages.get(index));
            index++;
        }));
        t.start();
    }

    private void setValue(int depositA, int depositB, int depositC, int depositD) {
        answer_A_input.getValueFactory().setValue(depositA);
        answer_A_input.getEditor().setText(Integer.toString(depositA));

        answer_B_input.getValueFactory().setValue(depositB);
        answer_B_input.getEditor().setText(Integer.toString(depositB));

        answer_C_input.getValueFactory().setValue(depositC);
        answer_C_input.getEditor().setText(Integer.toString(depositC));

        answer_D_input.getValueFactory().setValue(depositD);
        answer_D_input.getEditor().setText(Integer.toString(depositD));
    }

    public void sendMessage(ActionEvent event) {
        String message = chatbox_input.getText();

        if (!isNullOrEmpty(message)) {
            addMessage(message, false, Color.GREEN, true);
            SocketService.getInstance().roomChat(message, error -> {
                Label err = new Label("Failed to retrieve message from server. Trying again...");
                err.setTextFill(Color.RED);
                messages.add(err);
                chatBox.getChildren().add(messages.get(index));
                index++;
            });

        }
    }

    private void addMessage(String message, boolean isSystem, Color color, boolean resetChatbox) {
        Label m;
        if (isSystem) {
            m = new Label(message);
        } else {
            m = new Label(UserSession.getInstance().getName() + ": " + message);
        }
        m.setTextFill(color);
        messages.add(m);
        chatBox.getChildren().add(m);
        index++;

        if (resetChatbox) chatbox_input.setText("");
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
            value = currentMoney - B_input - C_input - D_input;
            answer_A_input.getValueFactory().setValue(value);
            answer_A_input.getEditor().setText(Integer.toString(value));
        } else if (button.equals(all_in_B)) {
            value = currentMoney - A_input - C_input - D_input;
            answer_B_input.getValueFactory().setValue(value);
            answer_B_input.getEditor().setText(Integer.toString(value));
        } else if (button.equals(all_in_C)) {
            value = currentMoney - A_input - B_input - D_input;
            answer_C_input.getValueFactory().setValue(value);
            answer_C_input.getEditor().setText(Integer.toString(value));
        } else if (button.equals(all_in_D)) {
            value = currentMoney - A_input - B_input - C_input;
            answer_D_input.getValueFactory().setValue(value);
            answer_D_input.getEditor().setText(Integer.toString(value));
        }
    }

    private void setTimer() {
        interval = 10;
        isSubmit = false;
        isAnswerCountdown = false;
        time_label.setText("Question in: ");
        time_left.setText("10s");
        isWait = false;
    }

    public void getQuestion() {
        if (UserSession.getInstance().isAlpha())
            SocketService.getInstance().getQuestion(error -> {
                Label m = new Label("Failed to retrieve message from server. Trying again...");
                m.setPrefWidth(chatBox.getPrefWidth());
                m.setTextFill(Color.RED);
                messages.add(m);
                chatBox.getChildren().add(messages.get(index));
                index++;
            });
    }

    public void toHome(ActionEvent event) {
        changeScreen(event, HOME_SCREEN);
    }

    public void onPressLeave(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm leave");
        alert.setHeaderText("You will lose your current credit. Do you really want to quit?");

        Optional<ButtonType> option = alert.showAndWait();

        if (option.get() == ButtonType.OK) {
            SocketService.getInstance().surrender(error -> {
            });
            toHome(event);
        }
    }

}
