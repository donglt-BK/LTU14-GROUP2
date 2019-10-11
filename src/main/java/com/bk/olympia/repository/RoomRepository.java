package com.bk.olympia.repository;

import com.bk.olympia.model.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Integer> {
    public Room findById(int id);
}
