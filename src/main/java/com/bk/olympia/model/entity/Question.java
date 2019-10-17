package com.bk.olympia.model.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "questionset")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private int difficulty;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TopicId")
    private Topic topic;

    @OneToMany(targetEntity = Answer.class, mappedBy = "question")
    private List<Answer> answers;

    public Question(@NotNull int difficulty, Topic topic, List<Answer> answers) {
        this.difficulty = difficulty;
        this.topic = topic;
        this.answers = answers;
    }

    public Question() {

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

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
