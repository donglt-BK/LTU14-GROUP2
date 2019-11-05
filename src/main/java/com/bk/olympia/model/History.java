package com.bk.olympia.model;

import com.bk.olympia.constant.ResultType;
import com.bk.olympia.model.entity.Room;
import com.bk.olympia.model.entity.User;

import java.util.Date;

public class History {
    private final int roomId;
    private final Date createdAt;
    private final Date endedAt;
    private final ResultType resultType;
    private final int balanceChanged;

    public History(Room room, User user) {
        this.roomId = room.getId();
        this.createdAt = room.getCreatedAt();
        this.endedAt = room.getEndedAt();
        this.resultType = room.getWinner() != user.getId() ? room.getWinner() == -1 ? ResultType.DRAW : ResultType.LOSE : ResultType.WIN;
        this.balanceChanged = room.getBetValue() * resultType.getModifier();
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

    public ResultType getResultType() {
        return resultType;
    }

    public int getBalanceChanged() {
        return balanceChanged;
    }
}
