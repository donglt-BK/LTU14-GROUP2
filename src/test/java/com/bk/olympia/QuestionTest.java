package com.bk.olympia;

import com.bk.olympia.model.entity.Question;
import com.bk.olympia.repository.QuestionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author tmduc
 * @package com.bk.olympia
 * @created 12/2/2019 10:01 AM
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class QuestionTest {
    @Autowired
    private QuestionRepository questionRepository;

    @Test
    public void testQuestion() {
        List<Question> questions = questionRepository.findAll();
        questions.forEach(q -> {
            System.out.println("Topic: " + q.getTopic().getTopicName() + ", difficulty " + q.getDifficulty());
            System.out.println("Question " + q.getId() + ": " + q.getQuestionDetail());
            for (int i = 0; i < q.getAnswers().size(); i++) {
                System.out.printf("%15s", q.getCorrectAnswer().getAnswerDetail().equals(q.getAnswers().get(i)) ? "x" : "");
                System.out.print((i + 1) + ". " + q.getAnswers().get(i));
                System.out.println();
            }
            System.out.println("-------------------------------------------------------------------------------------");
        });
    }
}
