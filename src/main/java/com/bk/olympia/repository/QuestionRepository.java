package com.bk.olympia.repository;

import com.bk.olympia.model.entity.Question;
import com.bk.olympia.model.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Question findById(int id);

    List<Question> findByTopicOrderByDifficultyAsc(Topic topic);

    List<Question> findByTopicAndIsAcceptedAndDifficultyAfterOrderByDifficultyAsc(Topic topic, boolean isAccepted, int difficulty);

//    @Query("SELECT q.id FROM Question q ORDER BY q.id DESC ")
//    public int getQuestionTotal();

    int findTopByOrderByIdDesc();
}
