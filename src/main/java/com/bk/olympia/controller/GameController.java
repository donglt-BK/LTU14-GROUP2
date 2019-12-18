package com.bk.olympia.controller;

import com.bk.olympia.base.BaseController;
import com.bk.olympia.constant.ContentType;
import com.bk.olympia.constant.Destination;
import com.bk.olympia.constant.MessageType;
import com.bk.olympia.event.DisconnectUserFromRoomEvent;
import com.bk.olympia.exception.AnswerPlacingViolationException;
import com.bk.olympia.exception.UnauthorizedActionException;
import com.bk.olympia.model.entity.*;
import com.bk.olympia.model.message.Message;
import com.google.common.util.concurrent.Striped;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import service.RandomService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class GameController extends BaseController implements ApplicationListener<ApplicationEvent> {
    private Striped<ReadWriteLock> lockStriped = Striped.lazyWeakReadWriteLock(32);

    @Override
    protected void init() {
        this.logger = LoggerFactory.getLogger(ChatController.class);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof DisconnectUserFromRoomEvent) {
            System.out.println("Player disconnect");
            userDisconnect(((DisconnectUserFromRoomEvent) event).getUser(), ((DisconnectUserFromRoomEvent) event).getPlayer(), ((DisconnectUserFromRoomEvent) event).getRoom());
        }
    }

    @MessageMapping("/play/ready")
    public void handleReady(@Payload Message message) {
        User user = findUserById(message.getSender());
        Player player = user.getCurrentPlayer();
        Room room = player.getRoom();

        handleReady(user, player, room);
    }

    private void handleReady(User user, Player player, Room room) {
        player.ready();
        if (room.isAllReady()) {
            Message m = new Message(MessageType.READY, user.getId());
            m.addContent(ContentType.READY, true);
            broadcast(room, Destination.READY, m);
            room.resetReady();
        }
        save(room);
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
//                for (int i : topicIds) {
//                    room.addTopic(topicRepository.findById(i));
//                }

                room.setTopics(Arrays.stream(topicIds)
                        .mapToObj(i -> new RoomTopic(room, topicRepository.findById(i), true))
                        .collect(Collectors.toList()));
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

        handleGetQuestion(user, player, room);
    }

    private void handleGetQuestion(User user, Player player, Room room) {
        if (room.getTopics().size() == 0) {
            List<Topic> totalTopic = topicRepository.findAll();
            int[] topicIds = RandomService.getRandomArray(totalTopic.size(), room.getMaxQuestions());

            room.setTopics(Arrays.stream(topicIds)
                    .mapToObj(i -> new RoomTopic(room, totalTopic.get(i), true))
                    .collect(Collectors.toList()));
            save(room);
        }
        Topic[] topics = room.getTopics().entrySet().stream().filter(Entry::getValue).map(Entry::getKey).toArray(Topic[]::new);
        int randomTopic = RandomService.getRandomInteger(0, room.getTopics().size() - 1);
        Topic topic = topics[randomTopic];
        room.setChosenTopic(topic);

        List<Question> questions = getQuestionList(topic, room.getCurrentLevel());
        int randomInteger = RandomService.getRandomInteger(questions.size());
        Question randomQuestion = questions.get(randomInteger);

        room.addLevel();
        roomRepository.save(room);

        Message m = new Message(MessageType.GET_QUESTION, user.getId());
        m.addContent(ContentType.QUESTION_ID, randomQuestion.getId())
                .addContent(ContentType.QUESTION, randomQuestion.getQuestionDetail())
                .addContent(ContentType.ANSWER, randomQuestion.getAnswers())
                .addContent(ContentType.DIFFICULTY, randomQuestion.getDifficulty());
        broadcast(room, Destination.GET_QUESTION, m);
    }

    private List<Question> getQuestionList(Topic topic, int minDifficulty) {
        return questionRepository.findByTopicAndIsAcceptedAndDifficultyAfterOrderByDifficultyAsc(topic, true, minDifficulty);
    }

    @MessageMapping("/play/submit-answer")
    public void processSubmitAnswer(@Payload Message message) {
        User user = findUserById(message.getSender());
        Player player = user.getCurrentPlayer();
        Room room = player.getRoom();
        Question question = questionRepository.findById((int) message.getContent(ContentType.QUESTION_ID));
        List<Integer> placement = message.getContent(ContentType.MONEY_PLACED);
        handleSubmitAnswer(user, player, room, question, placement);
    }

    private void handleSubmitAnswer(User user, Player player, Room room, Question question, List<Integer> moneyPlaced) {
        if (moneyPlaced.stream().mapToInt(Integer::intValue).sum() != player.getMoney())
            throw new AnswerPlacingViolationException(AnswerPlacingViolationException.ViolationType.MONEY_EXCEED_MONEY_TOTAL, user.getId());
        Answer correctAnswer = question.getCorrectAnswer();
        player.setMoney(moneyPlaced.get(question.getAnswers().indexOf(correctAnswer.getAnswerDetail())));
        save(player);

        Message m = new Message(MessageType.SUBMIT_ANSWER, user.getId());
        m.addContent(ContentType.ANSWER, question.getAnswers().indexOf(correctAnswer.getAnswerDetail()));
        m.addContent(ContentType.MONEY_PLACED, player.getMoney());
        broadcast(room, Destination.SUBMIT_ANSWER, m);
    }

    @MessageMapping("/play/get-answer")
    public void processGetCorrectAnswer(@Payload Message message) {
        User user = findUserById(message.getSender());
        Player player = user.getCurrentPlayer();
        Room room = player.getRoom();
        Question question = questionRepository.findById((int) message.getContent(ContentType.QUESTION_ID));
        handleGetCorrectAnswer(user, player, room, question);
    }

    private void handleGetCorrectAnswer(User user, Player player, Room room, Question question) {
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

    @MessageMapping("/play/surrender")
    public void surrender(@Payload Message message) {
        User user = findUserById(message.getSender());
        Player player = user.getCurrentPlayer();
        Room room = player.getRoom();
        player.setMoney(0);
        save(player);

        Message m = new Message(MessageType.SURRENDER, user.getId());
        broadcast(room, Destination.SURRENDER, m);
        handleGameOver(user, player, room);
    }

    private void handleGameOver(User user, Player player, Room room) {
        Player otherPlayer = room.getPlayerList().stream().filter(p-> p.getId() != player.getId()).collect(Collectors.toList()).get(0);

        if (player.getMoney() == 0) {
            Message m = new Message(MessageType.GAME_OVER, user.getId());
            m.addContent(ContentType.BET_VALUE, room.getBetValue());

            if (otherPlayer.getMoney() > 0) {
                user.addBalance(-room.getBetValue());
                otherPlayer.getUser().addBalance(room.getBetValue());
                room.setWinner(otherPlayer.getUser().getId());

                save(room);
                save(user);
                save(otherPlayer);

                m.addContent(ContentType.WINNER, new int[]{otherPlayer.getUser().getId()});
            } else if (otherPlayer.getMoney() == 0) {
                //DRAW
                m.addContent(ContentType.WINNER, new int[]{player.getUser().getId(), otherPlayer.getUser().getId()});
            }

            ReadWriteLock lock = lockStriped.get(room.getId());
            lock.readLock().lock();
            try {
                room.setEndedAt(LocalDateTime.now());
            } finally {
                lock.readLock().unlock();
                lock.writeLock().lock();
            }
            save(room);
            List<User> users = room.getPlayerList().stream().map(Player::getUser).collect(Collectors.toList());
            users.forEach(u -> u.setLobbyId(-1));
            userRepository.saveAll(users);

            broadcast(room, Destination.GAME_OVER, m);
        } else if (otherPlayer.getMoney() == 0) {
            Message m = new Message(MessageType.GAME_OVER, user.getId());
            m.addContent(ContentType.BET_VALUE, room.getBetValue());

            if (player.getMoney() > 0) {
                otherPlayer.getUser().addBalance(-room.getBetValue());
                user.addBalance(room.getBetValue());
                room.setWinner(player.getUser().getId());

                save(room);
                save(user);
                save(otherPlayer);

                m.addContent(ContentType.WINNER, new int[]{player.getUser().getId()});
            } else if (otherPlayer.getMoney() == 0) {
                //DRAW
                m.addContent(ContentType.WINNER, new int[]{player.getUser().getId(), otherPlayer.getUser().getId()});
            }

            ReadWriteLock lock = lockStriped.get(room.getId());
            lock.readLock().lock();
            try {
                room.setEndedAt(LocalDateTime.now());
            } finally {
                lock.readLock().unlock();
                lock.writeLock().lock();
            }
            save(room);
            List<User> users = room.getPlayerList().stream().map(Player::getUser).collect(Collectors.toList());
            users.forEach(u -> u.setLobbyId(-1));
            userRepository.saveAll(users);

            broadcast(room, Destination.GAME_OVER, m);
        }
    }

    private void userDisconnect(User user, Player player, Room room) {
        handleGameOver(user, player, room);
    }
}
