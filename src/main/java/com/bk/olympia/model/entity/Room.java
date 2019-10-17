package com.bk.olympia.model.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "room")
public class Room {
    private static final int DEFAULT_MAX_PLAYERS = 2;
    private static final int DEFAULT_MAX_QUESTIONS = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private int lobbyId;

    @NotNull
    private int maxUsers = DEFAULT_MAX_PLAYERS;

    @NotNull
    private int betValue;

    @NotNull
    private int currentLevel = 0;

    @NotNull
    private int maxQuestions = DEFAULT_MAX_QUESTIONS;


    @ManyToMany
    @JoinTable(
            name = "roomplayer",
            joinColumns = @JoinColumn(name = "roomid"),
            inverseJoinColumns = @JoinColumn(name = "playerid")
    )
    private List<Player> playerList;

    public Room(@NotNull int betValue) {
        this.betValue = betValue;
    }

    public Room(@NotNull int lobbyId, @NotNull int betValue, @NotNull List<Player> playerList) {
        this.lobbyId = lobbyId;
        this.betValue = betValue;
        this.playerList = playerList;
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

    public int getMaxUsers() {
        return maxUsers;
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
}
