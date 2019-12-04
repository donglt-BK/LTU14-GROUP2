package com.bk.olympia.UIFx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

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

    public void onPressLeave(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm leave");
        alert.setHeaderText("You will lose your current credit. Do you still dare to quit?");
        alert.getButtonTypes().clear();

        ButtonType confirm = new ButtonType("Confirm");
        ButtonType cancel = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(confirm, cancel);
        alert.showAndWait();

        Optional<ButtonType> option = alert.showAndWait();

        if (option.get() == confirm) {
            //TODO: subtract current credit to balance
            changeScreen(event, HOME_SCREEN);
        } else if (option.get() == cancel) {

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
            cancelBtn.setVisible(false);
        } else {
            depositA = Integer.parseInt(answerAInput.length() > 0 ? answerAInput : "0");
            depositB = Integer.parseInt(answerBInput.length() > 0 ? answerBInput : "0");
            depositC = Integer.parseInt(answerCInput.length() > 0 ? answerCInput : "0");
            depositD = Integer.parseInt(answerDInput.length() > 0 ? answerDInput : "0");

            cancelBtn.setVisible(true);
            confirmBtn.setVisible(false);
            //TODO: call api confirm answer
            //If confirm -> invisible cancelBtn
        }
    }

    public void onPressCancel(ActionEvent event) {
        answer_A_input.setText("");
        answer_B_input.setText("");
        answer_C_input.setText("");
        answer_D_input.setText("");
        cancelBtn.setVisible(false);
        confirmBtn.setVisible(true);
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
}
