package com.bk.olympia.controller;

import com.bk.olympia.base.BaseController;
import com.bk.olympia.model.entity.Player;
import com.bk.olympia.model.entity.Room;
import com.bk.olympia.model.entity.User;
import com.bk.olympia.model.message.ErrorMessage;
import com.bk.olympia.model.message.Message;
import com.bk.olympia.model.type.ContentType;
import com.bk.olympia.model.type.Destination;
import com.bk.olympia.model.type.ErrorType;
import com.bk.olympia.model.type.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import service.MessagingService;
import service.RandomService;

@Controller
public class GameController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    @MessageMapping("/play/load-complete")
    public void handleLoadComplete(@Payload Message message) {
        User user = findUserById(message.getSender());
        Player player = user.getCurrentPlayer();
        Room room = player.getRoom();

        player.ready();
        if (room.isAllReady()) {
            MessagingService.broadcast(room, Destination.LOAD_COMPLETE, new Message(MessageType.LOAD_COMPLETE));
        }
    }

    @MessageMapping("/play/get-topic-list")
    public void handleGetTopicList(@Payload Message message) {
        User user = findUserById(message.getSender());
        Player player = user.getCurrentPlayer();
        Room room = player.getRoom();

        if (room.isPlayerTurn(player)) {
            if (room.getTopics().size() == 0) {
                int totalTopic = topicRepository.findTopByOrderByIdDesc();
                int[] topicIds = RandomService.getRandomArray(totalTopic, room.getMaxQuestions());
                for (int i : topicIds) {
                    room.addTopic(topicRepository.findById(i));
                }
            }

            Message m = new Message(MessageType.GET_TOPIC_LIST, message.getSender());
            m.addContent(ContentType.TOPICS, room.getTopics());
            MessagingService.broadcast(room, Destination.GET_TOPIC_LIST, m);
        } else {
            MessagingService.sendTo(user, Destination.ERROR, new ErrorMessage(ErrorType.INVALID_ACTION, message.getSender()));
        }
    }
}
