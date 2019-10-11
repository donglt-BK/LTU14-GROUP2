package com.bk.olympia.model.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "questionset")
public class QuestionSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private int difficulty;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TopicId")
    private Topic topic;
    private Answer[] answers;

    public QuestionSet(@NotNull int difficulty, Topic topic, Answer[] answers) {
        this.difficulty = difficulty;
        this.topic = topic;
        this.answers = answers;
    }

    public QuestionSet() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Answer[] getAnswers() {
        return answers;
    }

    public void setAnswers(Answer[] answers) {
        this.answers = answers;
    }
}
