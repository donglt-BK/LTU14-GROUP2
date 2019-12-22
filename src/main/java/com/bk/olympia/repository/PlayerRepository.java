package com.bk.olympia.repository;

import com.bk.olympia.model.entity.Player;
import com.bk.olympia.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Integer> {
    List<Player> findByUser(User user);

    Player findByRoomIdAndUserIdNot(int roomId, int userId);
}
