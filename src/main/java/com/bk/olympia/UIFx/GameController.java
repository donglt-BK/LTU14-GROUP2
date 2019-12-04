package com.bk.olympia.UIFx;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

public class GameController {
    @FXML
    Hyperlink leave_game;
    @FXML
    Label money, time_left, question, answerA, answerB, answerC, answerD;
    @FXML
    TextField answer_A_input, answer_B_input, answer_C_input, answer_D_input;
    @FXML
    Button minus_A_10, plus_A_10, minus_A_100, plus_A_100, clear_A, all_in_A,
            minus_B_10, plus_B_10, minus_B_100, plus_B_100, clear_B, all_in_B,
            minus_C_10, plus_C_10, minus_C_100, plus_C_100, clear_C, all_in_C,
            minus_D_10, plus_D_10, minus_D_100, plus_D_100, clear_D, all_in_D,
            confirm, cancel, send_message;
    @FXML
    ScrollPane chatbox_scroll;
    @FXML
    AnchorPane chatbox;
    @FXML
    TextArea chatbox_input;

}
