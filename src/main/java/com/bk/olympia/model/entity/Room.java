package com.bk.olympia.model.entity;

import com.bk.olympia.base.IReadiable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Table(name = "room")
public class Room implements IReadiable {
    public static final int DEFAULT_MAX_PLAYERS = 2;
    public static final int DEFAULT_MAX_QUESTIONS = 10;

    @Transient
    private ArrayList<Boolean> readyList = new ArrayList<>();

    /**
     * A Map that contains the topics used for this current room.
     * The boolean value indicates whether the topic can be chosen (true) or not (false).
     */
    @Transient
    private Map<Topic, Boolean> topics = new TreeMap<>();

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

    @NotNull
    private int winner = -1;

    @NotNull
    private Date createdAt;

    @NotNull
    private Date endedAt;

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

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(Date endedAt) {
        this.endedAt = endedAt;
    }

    public void addTopic(Topic topic) {
        this.topics.put(topic, true);
    }

    public void setChosenTopic(Topic topic) {
        this.topics.put(topic, false);
    }

    public Map<Topic, Boolean> getTopics() {
        return topics;
    }

    public Integer[] getTopicIds() {
        return topics.keySet().stream()
                .map(Topic::getId)
                .toArray(Integer[]::new);
    }

    public String[] getTopicNames() {
        return topics.keySet().stream()
                .map(Topic::getTopicName)
                .toArray(String[]::new);
    }

    public String[] getTopicDescriptions() {
        return topics.keySet().stream()
                .map(Topic::getTopicDescription)
                .toArray(String[]::new);
    }

    public Boolean[] getTopicChosen() {
        return topics.values().toArray(new Boolean[maxQuestions]);
    }

    @Override
    public void addPlayerReady(int position) {
        this.readyList.add(position, true);
    }

    @Override
    public boolean isAllReady() {
        return this.readyList.stream().allMatch(val -> val);
    }

    @Override
    public void resetReady() {
        Collections.fill(readyList, Boolean.FALSE);
    }

    @Override
    public void removePlayerReady(int position) {

    }

    public boolean isPlayerTurn(Player player) {
        return player.getPosition() == (currentLevel % maxPlayers);
    }

    public void update() {
        playerList.forEach(p -> p.setRoom(this));
    }
}
