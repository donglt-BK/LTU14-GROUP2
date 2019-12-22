package com.bk.olympia.model;

import com.bk.olympia.constant.ResultType;
import com.bk.olympia.model.entity.Room;
import com.bk.olympia.model.entity.User;

import java.time.LocalDateTime;

public class History {
    private final int roomId;
    private final LocalDateTime createdAt;
    private final LocalDateTime endedAt;
    private final ResultType resultType;
    private final int balanceChanged;

    private final String opponent;

    public History(Room room, User user, String opponent) {
        this.roomId = room.getId();
        this.createdAt = room.getCreatedAt();
        this.endedAt = room.getEndedAt();
        this.resultType = room.getWinner() != user.getId() ? room.getWinner() == -1 ? ResultType.DRAW : ResultType.LOSE : ResultType.WIN;
        this.balanceChanged = room.getBetValue() * resultType.getModifier();
        this.opponent = opponent;
    }

    public int getRoomId() {
        return roomId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getEndedAt() {
        return endedAt;
    }

    public String getCreatedAtStr() {
        return getTimeStr(createdAt);
    }

    public String getEndedAtStr() {
        return getTimeStr(endedAt);
    }

    public ResultType getResultType() {
        return resultType;
    }

    public int getBalanceChanged() {
        return balanceChanged;
    }

    private String getTimeStr(LocalDateTime time) {
        if (time == null) return "";
        return time.getHour() + ":" + time.getMinute() + " " + time.getDayOfMonth() + "-" + time.getMonth().getValue() + "-" + time.getYear();
    }

    public String getOpponent() {
        return opponent;
    }
}
