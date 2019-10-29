package com.bk.olympia.event;

import com.bk.olympia.model.entity.Player;
import com.bk.olympia.model.entity.Room;
import com.bk.olympia.model.entity.User;
import org.springframework.context.ApplicationEvent;

public class DisconnectUserFromRoomEvent extends ApplicationEvent {
    private User user;
    private Player player;
    private Room room;

    public DisconnectUserFromRoomEvent(Object source, User user) {
        super(source);
        this.user = user;
        this.player = user.getCurrentPlayer();
        this.room = player.getRoom();
    }

    public User getUser() {
        return user;
    }

    public Player getPlayer() {
        return player;
    }

    public Room getRoom() {
        return room;
    }
}
