package com.bk.olympia.repository;

import com.bk.olympia.model.entity.Player;
import com.bk.olympia.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Integer> {
    List<Player> findByUser(User user);
}
