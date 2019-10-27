package com.bk.olympia.model.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@Entity
@Table(name = "room")
public class Room {
    public static final int DEFAULT_MAX_PLAYERS = 2;
    public static final int DEFAULT_MAX_QUESTIONS = 10;

    @Transient
    private ArrayList<Boolean> readyList = new ArrayList<>();

    /**
     * A Map that contains the topics used for this current room.
     * The boolean value indicates whether the topic can be chosen (true) or not (false).
     */
    @Transient
    private TreeMap<Topic, Boolean> topics = new TreeMap<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private int lobbyId;

    @NotNull
    private int maxPlayers = DEFAULT_MAX_PLAYERS;

    @NotNull
    private int betValue;

    @NotNull
    private int currentLevel = 0;

    @NotNull
    private int maxQuestions = DEFAULT_MAX_QUESTIONS;

    @ManyToMany
    @JoinTable(
            name = "room_player",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    private List<Player> playerList;

    public Room(@NotNull int betValue) {
        this.betValue = betValue;
    }

    public Room(@NotNull int lobbyId, @NotNull int betValue, @NotNull List<Player> playerList) {
        this.lobbyId = lobbyId;
        this.betValue = betValue;
        this.playerList = playerList;
        playerList.forEach(player -> player.setRoom(this));
    }

    public Room() {

    }

    public int getId() {
        return id;
    }

    public int getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(int lobbyId) {
        this.lobbyId = lobbyId;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getBetValue() {
        return betValue;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void addLevel() {
        this.currentLevel++;
    }

    public int getMaxQuestions() {
        return maxQuestions;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void addTopic(Topic topic) {
        this.topics.put(topic, true);
    }

    public void setChosenTopic(Topic topic) {
        this.topics.put(topic, false);
    }

    public TreeMap<Topic, Boolean> getTopics() {
        return topics;
    }

    public void addPlayerReady(int position) {
        this.readyList.add(position, true);
    }

    public boolean isAllReady() {
        return this.readyList.stream().allMatch(val -> val);
    }

    public boolean isPlayerTurn(Player player) {
        return player.getPosition() == (currentLevel % maxPlayers);
    }
}
