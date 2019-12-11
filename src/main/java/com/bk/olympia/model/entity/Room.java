package com.bk.olympia.model.entity;

import com.bk.olympia.base.IReadiable;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Table(name = "room")
public class Room implements IReadiable {
    public static final int DEFAULT_MAX_PLAYERS = 2;
    public static final int DEFAULT_MAX_QUESTIONS = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int lobbyId;

    @NotNull
    @Column(name = "max_users")
    private int maxPlayers = DEFAULT_MAX_PLAYERS;

    @NotNull
    private int betValue;

    @NotNull
    private int currentLevel = 0;

    @NotNull
    private int maxQuestions = DEFAULT_MAX_QUESTIONS;

    @NotNull
    private int winner = -1;

    @CreationTimestamp
    private LocalDateTime createdAt;
    private LocalDateTime endedAt;
    private int readyCount = 0;

    /**
     * A Map that contains the topics used for this current room.
     * The boolean value indicates whether the topic can be chosen (true) or not (false).
     */
    @OneToMany(fetch = FetchType.EAGER, targetEntity = RoomTopic.class, mappedBy = "room")
    private List<RoomTopic> topics;
//    @Transient
//    private Map<Topic, Boolean> topics = new TreeMap<>();

    @OneToMany(fetch = FetchType.LAZY, targetEntity = Player.class, mappedBy = "room")
    private List<Player> playerList;

    public Room(@NotNull int betValue) {
        this.betValue = betValue;
    }

    public Room(@NotNull int betValue, @NotNull List<Player> playerList) {
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(LocalDateTime endedAt) {
        this.endedAt = endedAt;
    }

    public Map<Topic, Boolean> getTopics() {
        return topics.stream().collect(Collectors.toMap(RoomTopic::getTopic, RoomTopic::isCanBeChosen));
    }

    public void setTopics(List<RoomTopic> topics) {
        this.topics = topics;
    }

    public void setChosenTopic(Topic topic) {
        this.topics.stream().filter(t -> t.getTopic().equals(topic)).findFirst().get().setCanBeChosen(false);
    }

    public Integer[] getTopicIds() {
        return topics.stream()
                .map(t -> t.getTopic().getId())
                .toArray(Integer[]::new);
    }

    public String[] getTopicNames() {
        return topics.stream()
                .map(t -> t.getTopic().getTopicName())
                .toArray(String[]::new);
    }

    public String[] getTopicDescriptions() {
        return topics.stream()
                .map(t -> t.getTopic().getTopicDescription())
                .toArray(String[]::new);
    }

    public Boolean[] getTopicChosen() {
        return topics.stream()
                .map(RoomTopic::isCanBeChosen)
                .toArray(Boolean[]::new);
    }

    @Override
    public void addPlayerReady(int position) {
        readyCount++;
    }

    @Override
    public boolean isAllReady() {
        return readyCount == maxPlayers;
    }

    @Override
    public void resetReady() {
        readyCount = 0;
    }

    @Override
    public void removePlayerReady(int position) {
        readyCount--;
    }

    public boolean isPlayerTurn(Player player) {
        return player.getPosition() == (currentLevel % maxPlayers);
    }

    public void update() {
        playerList.forEach(p -> p.setRoom(this));
    }
}
