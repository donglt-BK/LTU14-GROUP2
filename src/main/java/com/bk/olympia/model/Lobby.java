package com.bk.olympia.model;

import com.bk.olympia.base.IReadiable;
import com.bk.olympia.model.entity.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Lobby implements Comparable<Lobby>, IReadiable {
    private static final AtomicInteger autoId = new AtomicInteger(1);
    private static List<Integer> deletedIds = new ArrayList<>();

    private final int id;
    private String name = "Let's play!!";
    private ArrayList<User> users;
    private final int betValue;

    public Lobby(int betValue) {
        this.id = generateNewId();
        this.betValue = betValue;
        this.users = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public static void addDeletedId(int id) {
        deletedIds.add(id);
        Collections.sort(deletedIds);
    }

    public String getName() {
        return name;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public User getHost() {
        return users.get(0);
    }

    public Lobby addUser(User user) {
        user.setLobbyId(this.id);
        users.add(user);
        return this;
    }

    public boolean removeUser(User user) {
        user.setLobbyId(-1);
        return users.remove(user);
    }

    public int getBetValue() {
        return betValue;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int generateNewId() {
        if (deletedIds.size() == 0)
            return autoId.getAndIncrement();
        int id = deletedIds.get(0);
        deletedIds.remove(0);
        return id;
    }

    @Override
    public int compareTo(Lobby o) {
        return this.getId() - o.getId();
    }

    @Override
    public void addPlayerReady(int position) {
        this.readyList.add(position, true);
    }

    @Override
    public boolean isAllReady() {
        return this.readyList.stream().skip(1).allMatch(val -> val);
    }

    @Override
    public void resetReady() {

    }

    @Override
    public void removePlayerReady(int position) {
        this.readyList.add(position, false);
    }

    public List<Boolean> getReadyList() {
        return readyList;
    }
}
