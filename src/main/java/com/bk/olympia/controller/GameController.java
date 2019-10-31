package com.bk.olympia.controller;

import com.bk.olympia.base.BaseController;
import com.bk.olympia.base.BaseRuntimeException;
import com.bk.olympia.event.DisconnectUserFromRoomEvent;
import com.bk.olympia.exception.InvalidActionException;
import com.bk.olympia.model.entity.*;
import com.bk.olympia.model.message.ErrorMessage;
import com.bk.olympia.model.message.Message;
import com.bk.olympia.model.type.ContentType;
import com.bk.olympia.model.type.Destination;
import com.bk.olympia.model.type.ErrorType;
import com.bk.olympia.model.type.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import service.RandomService;

import java.util.List;

@Controller
public class GameController extends BaseController implements ApplicationListener<ApplicationEvent> {
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof DisconnectUserFromRoomEvent) {
            userDisconnect(((DisconnectUserFromRoomEvent) event).getUser(), ((DisconnectUserFromRoomEvent) event).getPlayer(), ((DisconnectUserFromRoomEvent) event).getRoom());
        }
    }

    @MessageMapping("/play/load-complete")
    public void processLoadComplete(@Payload Message message) {
        User user = findUserById(message.getSender());
        Player player = user.getCurrentPlayer();
        Room room = player.getRoom();

        handleLoadComplete(user, player, room);
    }

    private void handleLoadComplete(User user, Player player, Room room) {
        player.ready();
        if (room.isAllReady()) {
            broadcast(room, Destination.LOAD_COMPLETE, new Message(MessageType.LOAD_COMPLETE));
        }
    }

    @MessageMapping("/play/get-topic-list")
    public void processGetTopicList(@Payload Message message) {
        User user = findUserById(message.getSender());
        Player player = user.getCurrentPlayer();
        Room room = player.getRoom();

        handleGetTopicList(user, player, room);
    }

    private void handleGetTopicList(User user, Player player, Room room) {
        if (room.isPlayerTurn(player)) {
            if (room.getTopics().size() == 0) {
                int totalTopic = topicRepository.findTopByOrderByIdDesc();
                int[] topicIds = RandomService.getRandomArray(totalTopic, room.getMaxQuestions());
                for (int i : topicIds) {
                    room.addTopic(topicRepository.findById(i));
                }
            }

            Message m = new Message(MessageType.GET_TOPIC_LIST, user.getId());
            m.addContent(ContentType.TOPIC, room.getTopics());
            broadcast(room, Destination.GET_TOPIC_LIST, m);
        } else throw new InvalidActionException(user.getId());
    }

    @MessageMapping("/play/pick-topic")
    public void processPickTopic(@Payload Message message) {
        User user = findUserById(message.getSender());
        Player player = user.getCurrentPlayer();

        //TODO: Check if player.getRoom() already works or not!?
        Room room = roomRepository.getOne(player.getRoom().getId());
        handlePickTopic(user, player, room, message.getContent(ContentType.TOPIC), message);
    }

    private void handlePickTopic(User user, Player player, Room room, Topic topic, Message message) {
        if (room.isPlayerTurn(player) && room.getTopics().get(topic)) {
            room.setChosenTopic(topic);
            broadcast(room, Destination.PICK_TOPIC, message);
            save(room);
        } else throw new InvalidActionException(user.getId());
    }

    @MessageMapping("/play/get-question")
    public void processGetQuestion(@Payload Message message) {
        User user = findUserById(message.getSender());
        Player player = user.getCurrentPlayer();
        Room room = player.getRoom();
        handleGetQuestion(user, player, room, message.getContent(ContentType.TOPIC));
    }

    private void handleGetQuestion(User user, Player player, Room room, Topic topic) {
        if (room.isPlayerTurn(player)) {
            List<Question> questions = questionRepository.findByTopicAndDifficultyAfterOrderByDifficultyAsc(topic, room.getCurrentLevel());
            Message m = new Message(MessageType.GET_QUESTION, user.getId());
            m.addContent(ContentType.QUESTION, questions.get(RandomService.getRandomInteger(questions.size())));
            broadcast(room, Destination.GET_QUESTION, m);
        } else throw new InvalidActionException(user.getId());
    }

    private void userDisconnect(User user, Player player, Room room) {

    }

    @Override
    public Message handleException(BaseRuntimeException e) {
        logger.error(e.getMessage());
        if (e instanceof InvalidActionException)
            return new ErrorMessage(ErrorType.INVALID_ACTION, e.getUserId());
        return null;
    }
}
