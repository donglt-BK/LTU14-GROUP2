package com.bk.olympia;

import com.bk.olympia.model.Trivia;
import com.bk.olympia.model.entity.Answer;
import com.bk.olympia.model.entity.Question;
import com.bk.olympia.model.entity.Topic;
import com.bk.olympia.repository.AnswerRepository;
import com.bk.olympia.repository.QuestionRepository;
import com.bk.olympia.repository.TopicRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import service.RandomService;

import java.lang.reflect.Type;
import java.util.ArrayList;

@Service
public class DatabaseImport implements CommandLineRunner {
    private final Logger logger = LoggerFactory.getLogger(DatabaseImport.class);

    @Value("${spring.liquibase.drop-first}")
    private boolean isDropFirst;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private AnswerRepository answerRepository;

    public static int convertDifficulty(String difficulty) {
        switch (difficulty) {
            case "easy":
                return RandomService.getRandomInteger(1, 4);
            case "medium":
                return RandomService.getRandomInteger(5, 7);
            case "hard":
                return RandomService.getRandomInteger(8, 10);
            default:
                return 0;
        }
    }

    public static String convertHtmlCharacters(String s) {
        return StringEscapeUtils.unescapeHtml4(s);
    }

    public void init() {
        final String url = "https://opentdb.com/api.php?amount=100&type=multiple";
        Gson gson = new Gson();
        Question question;
        Topic topic;
        Answer answer;

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        assert response != null;
        JsonArray jsonArray = new JsonParser().parse(response).getAsJsonObject().getAsJsonArray("results");
        Type type = new TypeToken<ArrayList<Trivia>>() {
        }.getType();
        ArrayList<Trivia> trivias = gson.fromJson(jsonArray, type);

        for (Trivia trivia : trivias) {
            /**
             * Init Topic table
             */
            if (!topicRepository.findByTopicName(trivia.getCategory()).isPresent()) {
                topic = new Topic(trivia.getCategory(), trivia.getCategory());
                topicRepository.save(topic);
            }

            /**
             * Init Question table
             */
            topic = topicRepository.findByTopicName(trivia.getCategory()).get();
            question = new Question(topic, trivia.getQuestion(), trivia.getDifficulty());
            questionRepository.save(question);

            /**
             * Init Answer table
             */
            question = questionRepository.findByQuestionDetail(trivia.getQuestion());
            answer = new Answer(trivia.getCorrectAnswer(), true);
            answer.setQuestion(question);
            answerRepository.save(answer);

            for (String s : trivia.getIncorrectAnswers()) {
                answer = new Answer(s, false);
                answer.setQuestion(question);
                answerRepository.save(answer);
            }
        }
    }

    @Override
    public void run(String... args) throws Exception {
        if (isDropFirst) {
            logger.info("Init database");
            init();
            logger.info("Database init completed");
        }
    }
}
