package com.bk.olympia.controller;

import com.bk.olympia.base.BaseController;
import com.bk.olympia.base.BaseRuntimeException;
import com.bk.olympia.event.DisconnectUserFromRoomEvent;
import com.bk.olympia.exception.AnswerPlacingViolationException;
import com.bk.olympia.exception.UnauthorizedActionException;
import com.bk.olympia.model.entity.*;
import com.bk.olympia.model.message.ErrorMessage;
import com.bk.olympia.model.message.Message;
import com.bk.olympia.model.type.ContentType;
import com.bk.olympia.model.type.Destination;
import com.bk.olympia.model.type.ErrorType;
import com.bk.olympia.model.type.MessageType;
import com.google.common.util.concurrent.Striped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import service.RandomService;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.stream.IntStream;

@Controller
public class GameController extends BaseController implements ApplicationListener<ApplicationEvent> {
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);
    private Striped<ReadWriteLock> lockStriped = Striped.lazyWeakReadWriteLock(32);

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof DisconnectUserFromRoomEvent) {
            userDisconnect(((DisconnectUserFromRoomEvent) event).getUser(), ((DisconnectUserFromRoomEvent) event).getPlayer(), ((DisconnectUserFromRoomEvent) event).getRoom());
        }
    }

    @MessageMapping("/play/ready")
    public void processLoadComplete(@Payload Message message) {
        User user = findUserById(message.getSender());
        Player player = user.getCurrentPlayer();
        Room room = player.getRoom();

        handleLoadComplete(user, player, room);
    }

    private void handleLoadComplete(User user, Player player, Room room) {
        player.ready();
        if (room.isAllReady()) {
            broadcast(room, Destination.READY, new Message(MessageType.READY));
            room.resetReady();
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
                save(room);
            }

            Message m = new Message(MessageType.GET_TOPIC_LIST, user.getId());
            m.addContent(ContentType.TOPIC_ID, room.getTopicIds())
                    .addContent(ContentType.TOPIC_NAME, room.getTopicNames())
                    .addContent(ContentType.TOPIC_DESCRIPTION, room.getTopicDescriptions())
                    .addContent(ContentType.IS_TOPIC_CHOSEN, room.getTopicChosen());
            broadcast(room, Destination.GET_TOPIC_LIST, m);
        } else throw new UnauthorizedActionException(user.getId());
    }

    @MessageMapping("/play/pick-topic")
    public void processPickTopic(@Payload Message message) {
        User user = findUserById(message.getSender());
        Player player = user.getCurrentPlayer();

        //TODO: Check if player.getRoom() already works or not!?
        Room room = roomRepository.getOne(player.getRoom().getId());
        Topic topic = topicRepository.findById((int) message.getContent(ContentType.TOPIC_ID));
        handlePickTopic(user, player, room, topic);
    }

    private void handlePickTopic(User user, Player player, Room room, Topic topic) {
        if (room.isPlayerTurn(player) && room.getTopics().get(topic)) {
            room.setChosenTopic(topic);
            Message m = new Message(MessageType.PICK_TOPIC, user.getId());
            m.addContent(ContentType.TOPIC_ID, topic.getId())
                    .addContent(ContentType.TOPIC_NAME, topic.getTopicName())
                    .addContent(ContentType.TOPIC_DESCRIPTION, topic.getTopicDescription());
            broadcast(room, Destination.PICK_TOPIC, m);
            save(room);
        } else throw new UnauthorizedActionException(user.getId());
    }

    @MessageMapping("/play/get-question")
    public void processGetQuestion(@Payload Message message) {
        User user = findUserById(message.getSender());
        Player player = user.getCurrentPlayer();
        Room room = player.getRoom();
        Topic topic = topicRepository.findById((int) message.getContent(ContentType.TOPIC_ID));
        handleGetQuestion(user, player, room, topic);
    }

    private void handleGetQuestion(User user, Player player, Room room, Topic topic) {
        if (room.isPlayerTurn(player)) {
            List<Question> questions = getQuestionList(topic, room.getCurrentLevel());
            Message m = new Message(MessageType.GET_QUESTION, user.getId());
            Question randomQuestion = questions.get(RandomService.getRandomInteger(questions.size()));
            m.addContent(ContentType.QUESTION, randomQuestion.getQuestionDetail())
                    .addContent(ContentType.ANSWER, randomQuestion.getAnswers())
                    .addContent(ContentType.DIFFICULTY, randomQuestion.getDifficulty());
            broadcast(room, Destination.GET_QUESTION, m);
        } else throw new UnauthorizedActionException(user.getId());
    }

    @MessageMapping("/play/submit-answer")
    public void processSubmitAnswer(@Payload Message message) {
        User user = findUserById(message.getSender());
        Player player = user.getCurrentPlayer();
        Room room = player.getRoom();
        Question question = questionRepository.findById((int) message.getContent(ContentType.QUESTION));
        handleSubmitAnswer(user, player, room, question, message.getContent(ContentType.MONEY_PLACED));
    }

    private void handleSubmitAnswer(User user, Player player, Room room, Question question, int[] moneyPlaced) {
        //Check if player follows the rule: you have to put in all money you currently have and you can only place at max 3 answers
        if (IntStream.of(moneyPlaced).sum() != player.getMoney())
            throw new AnswerPlacingViolationException(AnswerPlacingViolationException.ViolationType.MONEY_EXCEED_MONEY_TOTAL, user.getId());
        if (IntStream.of(moneyPlaced).allMatch(m -> m > 0))
            throw new AnswerPlacingViolationException(AnswerPlacingViolationException.ViolationType.PLACE_ALL_FOUR_ANSWER, user.getId());
        Answer correctAnswer = question.getCorrectAnswer();
        player.setMoney(moneyPlaced[question.getAnswers().indexOf(correctAnswer)]);
        save(player);

        Message m = new Message(MessageType.SUBMIT_ANSWER, user.getId());
//        m.addContent(ContentType.ANSWER, correctAnswer.getAnswer());
        broadcast(room, Destination.SUBMIT_ANSWER, m);
    }

    @MessageMapping("/play/get-answer")
    public void processGetAnswer(@Payload Message message) {
        User user = findUserById(message.getSender());
        Player player = user.getCurrentPlayer();
        Room room = player.getRoom();
        Question question = questionRepository.findById((int) message.getContent(ContentType.QUESTION));
        handleGetAnswer(user, player, room, question);
    }

    private void handleGetAnswer(User user, Player player, Room room, Question question) {
        if (room.isPlayerTurn(player)) {
            room.addLevel();
            save(room);

            Message m = new Message(MessageType.GET_ANSWER, user.getId());
            m.addContent(ContentType.ANSWER, question.getCorrectAnswer().getAnswerDetail());
            broadcast(room, Destination.GET_ANSWER, m);
        } else throw new UnauthorizedActionException(user.getId());
    }

    @MessageMapping("/play/game-over")
    public void processGameOver(@Payload Message message) {
        User user = findUserById(message.getSender());
        Player player = user.getCurrentPlayer();
        Room room = player.getRoom();
        handleGameOver(user, player, room);
    }

    private void handleGameOver(User user, Player player, Room room) {
        if (player.getMoney() == 0) {
            Message m = new Message(MessageType.GAME_OVER, user.getId());

            if (room.getPlayerList().size() == Room.DEFAULT_MAX_PLAYERS) {
                Player otherPlayer = Collections.max(room.getPlayerList(), Comparator.comparing(Player::getMoney));
                if (otherPlayer.getMoney() > 0) {
                    otherPlayer.getUser().addBalance(room.getBetValue());
                    save(otherPlayer);

                    m.addContent(ContentType.WINNER, otherPlayer.getId());
                } else if (otherPlayer.getMoney() == 0) {
                    ReadWriteLock lock = lockStriped.get(room.getId());
                    lock.readLock().lock();
                    try {
                        m.addContent(ContentType.WINNER, new int[]{player.getId(), otherPlayer.getId()});
                        broadcast(room, Destination.GAME_OVER, m);
                    } finally {
                        lock.readLock().unlock();
                        lock.writeLock().lock();
                    }
                    return;
                }
            } else {
                room.getPlayerList().remove(player);
                m.addContent(ContentType.WINNER, -1);
            }
            player.getUser().addBalance(-room.getBetValue());
            save(player);
            broadcast(room, Destination.GAME_OVER, m);
        }
    }

    private void userDisconnect(User user, Player player, Room room) {
        handleGameOver(user, player, room);
    }

    private List<Question> getQuestionList(Topic topic, int minDifficulty) {
        return questionRepository.findByTopicAndIsAcceptedAndDifficultyAfterOrderByDifficultyAsc(topic, true, minDifficulty);
    }

    @Override
    public Message handleException(BaseRuntimeException e) {
        logger.error(e.getMessage());
        if (e instanceof UnauthorizedActionException)
            return new ErrorMessage(ErrorType.INVALID_ACTION, e.getUserId());
        if (e instanceof AnswerPlacingViolationException)
            return new ErrorMessage(ErrorType.ANSWER_PLACING_VIOLATION, e.getUserId());
        return null;
    }
}
