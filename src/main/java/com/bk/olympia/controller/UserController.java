package com.bk.olympia.controller;

import com.bk.olympia.base.BaseController;
import com.bk.olympia.base.HistoryList;
import com.bk.olympia.base.TopicList;
import com.bk.olympia.constant.ContentType;
import com.bk.olympia.constant.Destination;
import com.bk.olympia.constant.MessageType;
import com.bk.olympia.event.DisconnectUserFromLobbyEvent;
import com.bk.olympia.event.DisconnectUserFromRoomEvent;
import com.bk.olympia.model.entity.Answer;
import com.bk.olympia.model.entity.Question;
import com.bk.olympia.model.entity.Topic;
import com.bk.olympia.model.entity.User;
import com.bk.olympia.model.message.Message;
import com.bk.olympia.model.message.MessageAccept;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController extends BaseController {

    @Override
    protected void init() {
        this.logger = LoggerFactory.getLogger(UserController.class);
    }

    @MessageMapping("/user/get-info")
    @SendToUser(Destination.GET_USER_INFO)
    public Message processGetInfo(@Payload Message message) {
        User user = findUserById(message.getSender());
        return handleGetInfo(user);
    }

    private Message handleGetInfo(User user) {
        Message m = new Message(MessageType.GET_INFO, user.getId());
        m.addContent(ContentType.USER_ID, user.getId())
                .addContent(ContentType.USERNAME, user.getUsername())
                .addContent(ContentType.NAME, user.getName())
                .addContent(ContentType.GENDER, user.getGender())
                .addContent(ContentType.BALANCE, user.getBalance())
                .pack();
        return m;
    }

    @MessageMapping("/user/change-info")
    @SendToUser(Destination.CHANGE_USER_INFO)
    public Message processSetInfo(@Payload Message message) {
        User user = userRepository.getOne(message.getSender());
        return handleSetInfo(user, message);
    }

    private Message handleSetInfo(User user, Message message) {
        message.getContent().forEach((k, v) -> {
            if (k instanceof ContentType) {
                ContentType key = (ContentType) k;

                switch (key) {
                    case NAME:
                        user.setName((String) v);
                        break;
                    case GENDER:
                        user.setGender((int) v);
                        break;
                }
            }
        });
        userRepository.save(user);
        return new Message(MessageType.CHANGE_INFO, message.getSender()).pack();
    }

    @MessageMapping("/user/get-recent-history")
    @SendToUser(Destination.GET_RECENT_HISTORY)
    public Message processGetHistory(@Payload Message message) {
        User user = findUserById(message.getSender());
        return handleGetHistory(user);
    }

    private Message handleGetHistory(User user) {
        HistoryList historyList = new HistoryList(user);
        Message m = new Message(MessageType.GET_RECENT_HISTORY, user.getId());
        m.addContent(ContentType.HISTORY_ROOM_ID, historyList.getHistoryRoomIds())
                .addContent(ContentType.HISTORY_CREATED_AT, historyList.getCreatedAts())
                .addContent(ContentType.HISTORY_ENDED_AT, historyList.getEndedAts())
                .addContent(ContentType.HISTORY_RESULT_TYPE, historyList.getResultTypes())
                .addContent(ContentType.HISTORY_BALANCE_CHANGED, historyList.getBalanceChanges());
        return m;
    }

    @MessageMapping("/user/get-all-topics")
    @SendToUser(Destination.GET_ALL_TOPICS)
    public Message processGetAllTopics(@Payload Message message) {
        return handleGetAllTopics(message);
    }

    private Message handleGetAllTopics(Message message) {
        TopicList topicList = new TopicList(topicRepository.findAll());
        Message m = new Message(MessageType.GET_ALL_TOPICS, message.getSender());
        m.addContent(ContentType.TOPIC_ID, topicList.getTopicIds())
                .addContent(ContentType.TOPIC_NAME, topicList.getTopicNames())
                .addContent(ContentType.TOPIC_DESCRIPTION, topicList.getTopicDescriptions());
        return m;
    }

    @MessageMapping("/user/add-question")
    @SendToUser(Destination.ADD_QUESTION)
    public Message processAddQuestion(@Payload Message message) {
        User user = findUserById(message.getSender());
        Topic topic = findTopicById(message.getContent(ContentType.TOPIC_ID));
        return handleAddQuestion(user, topic, message.getContent(ContentType.QUESTION), message.getContent(ContentType.ANSWER), message.getContent(ContentType.CORRECT_ANSWER_POSITION));
    }

    private Message handleAddQuestion(User user, Topic topic, String question, List<String> answers, int correctAnswerPos) {
        //TODO: Add question and check if needed
        List<Answer> answerList = new ArrayList<>();
        for (int i = 0; i < answers.size(); i++) {
            answerList.add(new Answer(answers.get(i), correctAnswerPos == i));
        }
        Question q = new Question(topic, question, answerList);

        for (Answer a : answerList) {
            a.setQuestion(q);
        }
        questionRepository.save(q);
        answerRepository.saveAll(answerList);
        return new MessageAccept(MessageType.ADD_QUESTION, user.getId());
    }

    @MessageMapping("/user/logout")
    @SendToUser(Destination.LOGOUT)
    public Message processLogout(@Payload Message message) {
        User user = findUserById(message.getSender());
        return handleLogout(user);
    }

    private Message handleLogout(User user) {
        if (user.getLobbyId() >= 0)
            publishEvent(new DisconnectUserFromLobbyEvent(this, user));
        else {
            publishEvent(new DisconnectUserFromRoomEvent(this, user));
        }
        return null;
    }
}
