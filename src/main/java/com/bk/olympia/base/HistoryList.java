package com.bk.olympia.base;

import com.bk.olympia.model.History;
import com.bk.olympia.model.entity.Player;
import com.bk.olympia.model.entity.Room;
import com.bk.olympia.model.entity.User;

import java.util.ArrayList;
import java.util.Date;

public class HistoryList {
    private ArrayList<History> list;

    public HistoryList(User user) {
        ArrayList<Player> playerHistory = (ArrayList<Player>) user.getPlayerList();
        ArrayList<Room> roomHistory = new ArrayList<>();
        playerHistory.forEach(p -> roomHistory.add(p.getRoom()));

        list = new ArrayList<>();
        roomHistory.forEach(room -> {
            list.add(new History(room, user));
        });
    }

    public int[] getHistoryRoomIds() {
        return list.stream()
                .mapToInt(History::getRoomId)
                .toArray();
    }

    public Date[] getCreatedAts() {
        return list.stream()
                .map(History::getCreatedAt)
                .toArray(Date[]::new);
    }

    public Date[] getEndedAts() {
        return list.stream()
                .map(History::getEndedAt)
                .toArray(Date[]::new);
    }

    public int[] getResultTypes() {
        return list.stream()
                .mapToInt(e -> e.getResultType().getModifier())
                .toArray();
    }

    public int[] getBalanceChanges() {
        return list.stream()
                .mapToInt(History::getBalanceChanged)
                .toArray();
    }
}
