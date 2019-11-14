package com.bk.olympia.model.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "question_set")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "question")
    private String questionDetail;

    @NotNull
    private int difficulty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @OneToMany(targetEntity = Answer.class, mappedBy = "question")
    @Size(max = 4)
    private List<Answer> answers;

    private boolean isAccepted;

    public Question(Topic topic, String questionDetail, List<Answer> answers) {
        this.topic = topic;
        this.questionDetail = questionDetail;
        this.answers = answers;
        this.isAccepted = false;
    }

    public Question(Topic topic, String questionDetail, int difficulty) {
        this.topic = topic;
        this.questionDetail = questionDetail;
        this.difficulty = difficulty;
        this.isAccepted = true;
    }

    public Question() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestionDetail() {
        return questionDetail;
    }

    public void setQuestionDetail(String questionDetail) {
        this.questionDetail = questionDetail;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public List<String> getAnswers() {
        return answers.stream().map(Answer::getAnswerDetail).collect(Collectors.toList());
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public Answer getCorrectAnswer() {
        return answers.stream()
                .filter(Answer::isCorrect)
                .findFirst()
                .orElse(null);
    }
}
