package com.bk.olympia.model;

import com.bk.olympia.model.entity.Room;
import com.bk.olympia.model.entity.User;

import java.util.Date;

public class History {
    private final int roomId;
    private final Date createdAt;
    private final Date endedAt;
    private final boolean isWon;
    private final int balanceChanged;

    public History(Room room, User user) {
        this.roomId = room.getId();
        this.createdAt = room.getCreatedAt();
        this.endedAt = room.getEndedAt();
        this.isWon = room.getWinner() == user.getId();
        this.balanceChanged = isWon ? room.getBetValue() : -room.getBetValue();
    }

    public int getRoomId() {
        return roomId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getEndedAt() {
        return endedAt;
    }

    public boolean isWon() {
        return isWon;
    }

    public int getBalanceChanged() {
        return balanceChanged;
    }
}
