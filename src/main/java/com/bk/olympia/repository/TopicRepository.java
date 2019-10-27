package com.bk.olympia.repository;

import com.bk.olympia.model.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Integer> {
    Topic findById(int id);

    int findTopByOrderByIdDesc();
}
