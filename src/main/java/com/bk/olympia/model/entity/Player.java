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

    private int moneyLeft;

    private static Player instance;

    private Player(@NotNull int id, @NotNull int moneyLeft) {
        this.id = id;
        this.moneyLeft = moneyLeft;
    }

    public Player() {

    }

    public static Player getInstance(@NotNull int id, @NotNull int betValue) {
        if (instance == null) {
            synchronized (Player.class) {
                if (instance == null)
                    instance = new Player(id, betValue);
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

    public int getMoneyLeft() {
        return moneyLeft;
    }

    public void setMoneyLeft(int totalPoint) {
        this.moneyLeft = totalPoint;
    }

//    public void resetPlayer() {
//        this.moneyLeft = 0;
//    }
}
