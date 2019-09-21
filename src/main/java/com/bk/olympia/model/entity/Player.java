package com.bk.olympia.model.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "PlayerData")
public class Player {
    @NotNull
    @Id
    private int id;

    private int currentLevel;
    private int totalPoint;

    private static Player instance;

    private Player(@NotNull int id) {
        this.id = id;
        currentLevel = totalPoint = 0;
    }

    public Player() {

    }

    public static Player getInstance(@NotNull int id) {
        if (instance == null) {
            synchronized (Player.class) {
                if (instance == null)
                    instance = new Player(id);
            }
        }
        return instance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public int getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(int totalPoint) {
        this.totalPoint = totalPoint;
    }

    public void resetPlayer() {
        this.totalPoint = this.currentLevel = 0;
    }
}
