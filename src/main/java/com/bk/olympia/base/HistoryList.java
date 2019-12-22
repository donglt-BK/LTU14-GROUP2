package com.bk.olympia.base;

import com.bk.olympia.model.History;
import com.bk.olympia.model.entity.Player;
import com.bk.olympia.model.entity.Room;
import com.bk.olympia.model.entity.User;
import com.bk.olympia.repository.PlayerRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class HistoryList {
    private ArrayList<History> list;

    public HistoryList(User user, PlayerRepository playerRepository) {
        List<Player> playerHistory = user.getPlayerList();
        ArrayList<Room> roomHistory = new ArrayList<>();
        playerHistory.forEach(p -> roomHistory.add(p.getRoom()));

        list = new ArrayList<>();
        roomHistory.forEach(room -> {
            Player p = playerRepository.findByRoomIdAndUserIdNot(room.getId(), user.getId());
            list.add(new History(room, user, p.getUser().getName()));
        });
    }

    public int[] getHistoryRoomIds() {
        return list.stream()
                .mapToInt(History::getRoomId)
                .toArray();
    }

    public String[] getCreatedAts() {
        return list.stream()
                .map(History::getCreatedAtStr)
                .toArray(String[]::new);
    }

    public String[] getEndedAts() {
        return list.stream()
                .map(History::getEndedAtStr)
                .toArray(String[]::new);
    }

    public String[] getResultTypes() {
        return list.stream()
                .map(e -> e.getResultType().getString())
                .toArray(String[]::new);
    }

    public int[] getBalanceChanges() {
        return list.stream()
                .mapToInt(History::getBalanceChanged)
                .toArray();
    }
    public String[] getOpponents() {
        return list.stream()
                .map(History::getOpponent)
                .toArray(String[]::new);
    }
}
