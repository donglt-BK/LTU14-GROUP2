package com.bk.olympia.repository;

import com.bk.olympia.model.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    List<Answer> findByQuestion_Id(int id);

    Answer findByQuestion_IdAndIsCorrect(int id, boolean isCorrect);
}
