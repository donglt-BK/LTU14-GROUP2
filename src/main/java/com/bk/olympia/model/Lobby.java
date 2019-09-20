package com.bk.olympia.model;

import com.bk.olympia.model.entity.User;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Lobby {
    private static final AtomicInteger autoId = new AtomicInteger(0);
    private final int id;
    private ArrayList<User> users;
    private final int betValue;

    public Lobby(int betValue) {
        this.id = autoId.getAndIncrement();
        this.betValue = betValue;
    }

    public Lobby(ArrayList<User> users, int betValue) {
        this.id = autoId.getAndIncrement();
        this.users = users;
        this.betValue = betValue;
    }

    public int getId() {
        return id;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public User getFirstUser() {
        return users.get(0);
    }

    public boolean addUser(User user) {
        return users.add(user);
    }

    public boolean removeUser(User user) {
        return users.remove(user);
    }

    public int getBetValue() {
        return betValue;
    }
}
