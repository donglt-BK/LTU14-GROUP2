package com.bk.olympia.UIFx;

import com.bk.olympia.request.socket.SocketService;
import com.bk.olympia.type.ContentType;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;

import java.util.*;
import java.util.stream.Collectors;

import static com.bk.olympia.config.Constant.HOME_SCREEN;

public class ContributeController extends ScreenService {
    private static class Topic {
        public int id;
        public String name;

        public Topic(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    public ComboBox<Topic> selectTopic;
    public TextField question;
    public TextField correctAnswer;
    public TextField incorrectAnswer1;
    public TextField incorrectAnswer2;
    public TextField incorrectAnswer3;
    public Label message;

    public void initialize() {
        selectTopic.setConverter(new StringConverter<Topic>() {
            @Override
            public String toString(Topic topic) {
                return topic.name;
            }

            @Override
            public Topic fromString(String string) {
                return null;
            }
        });

        SocketService.getInstance().getTopic(
                message -> Platform.runLater(() -> {
                    List<Double> id = message.getContent(ContentType.TOPIC_ID);
                    List<String> name = message.getContent(ContentType.TOPIC_NAME);

                    Set<Topic> topics = new LinkedHashSet<>();
                    //topics.add(new Topic(-1, "Add new topic"));

                    for (int i = 0; i < id.size(); i++) {
                        topics.add(new Topic(id.get(i).intValue(), name.get(i)));
                    }
                    selectTopic.getItems().addAll(topics);

                    onPressClear(null);
                }),
                error -> {
                }
        );
    }

    public void onPressRegister(ActionEvent event) {
        message.setTextFill(Color.RED);
        String question = this.question.getText();
        if (question.equals("")) {
            message.setText("Missing question");
            return;
        }

        String correctAnswer = this.correctAnswer.getText();
        if (correctAnswer.equals("")) {
            message.setText("Missing correct answer");
            return;
        }

        String[] incorrectAnswer = {
                this.incorrectAnswer1.getText(),
                this.incorrectAnswer2.getText(),
                this.incorrectAnswer3.getText()
        };
        for (String incorrect : incorrectAnswer) {
            if (incorrect.equals("")) {
                message.setText("Missing incorrect answer");
                return;
            }
        }

        int topic = selectTopic.getSelectionModel().getSelectedItem().id;

        //check
        System.out.println(question);
        System.out.println(correctAnswer);
        System.out.println(Arrays.toString(incorrectAnswer));
        System.out.println(topic);

        List<String> answers = Arrays.stream(incorrectAnswer).collect(Collectors.toList());
        answers.add(correctAnswer);
        Collections.shuffle(answers);
        String[] answer = answers.toArray(new String[0]);
        int correctAnswerPos = 1;
        for (int i = 0; i < answer.length; i++) {
            if (answer[i].equals(correctAnswer)) {
                correctAnswerPos = i;
                break;
            }
        }

        SocketService.getInstance().addQuestion(topic, question, answer, correctAnswerPos,
                message -> Platform.runLater(() -> {
                    this.message.setText("Successful");
                    this.message.setTextFill(Color.GREEN);
                }),
                error -> Platform.runLater(() -> {
                    this.message.setText("Error sending question");
                    this.message.setTextFill(Color.RED);
                })
        );
    }

    public void onPressClear(ActionEvent event) {
        selectTopic.getSelectionModel().select(0);
        question.setText("");
        correctAnswer.setText("");
        incorrectAnswer1.setText("");
        incorrectAnswer2.setText("");
        incorrectAnswer3.setText("");
        message.setText("");
    }

    public void onPressGoback(ActionEvent event) {
        changeScreen(event, HOME_SCREEN);
    }
}
