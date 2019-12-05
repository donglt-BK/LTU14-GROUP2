package com.bk.olympia.model;

import com.bk.olympia.base.IReadiable;
import com.bk.olympia.model.entity.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Lobby implements Comparable<Lobby>, IReadiable {
    private static final AtomicInteger autoId = new AtomicInteger(1);
    private static List<Integer> deletedIds = new ArrayList<>();

    private final int id;
    private String name = "Let's play!!";
    private ArrayList<User> users;
    private final int betValue;
    private Map<Integer, Boolean> readyList;

    public Lobby(int betValue) {
        readyList = Stream.of(
                new AbstractMap.SimpleEntry<>(1, false),
                new AbstractMap.SimpleEntry<>(2, false)
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
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
        this.readyList.put(position, true);
    }

    @Override
    public boolean isAllReady() {
        return this.readyList.values().stream().allMatch(val -> val);
    }

    @Override
    public void resetReady() {

    }

    @Override
    public void removePlayerReady(int position) {
        this.readyList.put(position, false);
    }

    public List<Boolean> getReadyList() {
        return new ArrayList<>(readyList.values());
    }
}
