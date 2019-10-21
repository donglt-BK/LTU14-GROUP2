package com.bk.olympia.model.entity;

import com.bk.olympia.model.Lobby;
import com.bk.olympia.model.message.Message;
import com.bk.olympia.model.type.ContentType;
import com.bk.olympia.model.type.MessageType;
import service.MessagingService;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String name;

    @NotNull
    private int gender;

    private int balance;

    public User() {

    }

    public User(@NotNull String username, @NotNull String password) {
        this.username = username;
        this.password = password;
        this.name = "Player " + this.id;
        this.gender = Gender.OTHER.getValue();
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

    public void join(Lobby lobby) {
        Message m = new Message(MessageType.JOIN_LOBBY, this.getId());
        m.addContent(ContentType.LOBBY_ID, lobby.getId())
                .addContent(ContentType.LOBBY_PARTICIPANT, lobby.getHost().getName());
        lobby.addUser(this);

        MessagingService.broadcast(lobby.getUsers(), "/queue/play/join", m);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User)
            return this.getId() == ((User) obj).getId();
        else return false;
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
