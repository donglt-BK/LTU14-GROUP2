package com.bk.olympia.base;

import com.bk.olympia.model.entity.Topic;

import java.util.ArrayList;
import java.util.List;

public class TopicList {
    private ArrayList<Topic> topics;

    public TopicList(List<Topic> topics) {
        this.topics = (ArrayList<Topic>) topics;
    }

    public Integer[] getTopicIds() {
        return topics.stream()
                .map(Topic::getId)
                .toArray(Integer[]::new);
    }

    public String[] getTopicNames() {
        return topics.stream()
                .map(Topic::getTopicName)
                .toArray(String[]::new);
    }

    public String[] getTopicDescriptions() {
        return topics.stream()
                .map(Topic::getTopicDescription)
                .toArray(String[]::new);
    }
}
