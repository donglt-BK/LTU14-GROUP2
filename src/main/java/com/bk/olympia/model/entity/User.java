package com.bk.olympia.model.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String name;

    private int gender;
    private int balance;

    public User() {

    }

    public User(@NotNull String username, @NotNull String password) {
        this.username = username;
        this.password = password;
    }

    public User(@NotNull String username, @NotNull String password, @NotNull String name, int gender) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.balance = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public enum Gender {
        MALE(1),
        FEMALE(2),
        OTHER(3);

        int value;

        Gender(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
