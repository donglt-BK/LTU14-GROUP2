package com.bk.olympia.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@Entity
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    private final int maxUsers;

    @NotNull
    private final int betValue;

    @NotNull
    private final int maxQuestions;

    //TODO: Join table RoomPlayer
    private ArrayList<Player> playerList;

    public Room(@NotNull int maxUsers, @NotNull int betValue, @NotNull int maxQuestions) {
        this.maxUsers = maxUsers;
        this.betValue = betValue;
        this.maxQuestions = maxQuestions;
    }

    public Room(@NotNull int maxUsers, @NotNull int betValue, @NotNull int maxQuestions, @NotNull ArrayList<Player> playerList) {
        this.maxUsers = maxUsers;
        this.betValue = betValue;
        this.maxQuestions = maxQuestions;
        this.playerList = playerList;
    }

    public int getId() {
        return id;
    }

    public int getMaxUsers() {
        return maxUsers;
    }

    public int getBetValue() {
        return betValue;
    }

    public int getMaxQuestions() {
        return maxQuestions;
    }

    public void setPlayerList(ArrayList<Player> playerList) {
        this.playerList = playerList;
    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }
}
