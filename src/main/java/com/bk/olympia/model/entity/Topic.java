package com.bk.olympia.model.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "topic")
public class Topic implements Comparable<Topic> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(unique = true)
    private String topicName;

    private String topicDescription;

    public Topic(@NotNull String topicName) {
        this.topicName = topicName;
    }

    public Topic(@NotNull String topicName, String topicDescription) {
        this.topicName = topicName;
        this.topicDescription = topicDescription;
    }

    public Topic() {
        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicDescription() {
        return topicDescription;
    }

    public void setTopicDescription(String topicDescription) {
        this.topicDescription = topicDescription;
    }

    @Override
    public int compareTo(Topic t) {
        return this.topicName.compareTo(t.getTopicName());
    }
}
