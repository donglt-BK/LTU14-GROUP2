package com.bk.olympia.model.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    private int maxUsers;

    @NotNull
    private int betValue;

    @NotNull
    private int maxQuestions;

    @ManyToMany
    @JoinTable(
            name = "roomplayer",
            joinColumns = @JoinColumn(name = "roomid"),
            inverseJoinColumns = @JoinColumn(name = "playerid")
    )
    private List<Player> playerList;

    public Room(@NotNull int maxUsers, @NotNull int betValue, @NotNull int maxQuestions) {
        this.maxUsers = maxUsers;
        this.betValue = betValue;
        this.maxQuestions = maxQuestions;
    }

    public Room(@NotNull int maxUsers, @NotNull int betValue, @NotNull int maxQuestions, @NotNull List<Player> playerList) {
        this.maxUsers = maxUsers;
        this.betValue = betValue;
        this.maxQuestions = maxQuestions;
        this.playerList = playerList;
    }

    public Room() {

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

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }
}
