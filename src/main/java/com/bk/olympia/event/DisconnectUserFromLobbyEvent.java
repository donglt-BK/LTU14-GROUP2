package com.bk.olympia.event;

import com.bk.olympia.model.entity.User;
import org.springframework.context.ApplicationEvent;

public class DisconnectUserFromLobbyEvent extends ApplicationEvent {
    private User user;
    private int lobbyId;

    public DisconnectUserFromLobbyEvent(Object source, User user) {
        super(source);
        this.user = user;
        this.lobbyId = user.getLobbyId();
    }

    public User getUser() {
        return user;
    }

    public int getLobbyId() {
        return lobbyId;
    }
}
