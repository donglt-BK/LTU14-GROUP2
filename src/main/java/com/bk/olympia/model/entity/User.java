package com.bk.olympia.model.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "user")
public class User {
    private static final int DEFAULT_HISTORY_SIZE = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String uid;

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String name;

    @NotNull
    private int gender;

    private int balance = 50000;

    @OneToMany(targetEntity = Player.class, mappedBy = "user", fetch = FetchType.EAGER)
    @OrderBy("id DESC")
    @Size(max = DEFAULT_HISTORY_SIZE)
    private List<Player> playerList = new ArrayList<>();

    private int lobbyId = -1;

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
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public void addBalance(int balance) {
        this.balance += balance;
    }

    public int getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(int lobbyId) {
        this.lobbyId = lobbyId;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void addPlayer(Player player) {
        this.playerList.add(0, player);

        //Truncate list to maximum size
        playerList = playerList
                .stream()
                .limit(DEFAULT_HISTORY_SIZE)
                .collect(Collectors.toList());
    }

    public Player getCurrentPlayer() {
        return playerList.get(0);
    }

//    public void join(Lobby lobby) {
////        Message m = new Message(MessageType.JOIN_LOBBY, this.getId());
////        m.addContent(ContentType.LOBBY_ID, lobby.getId())
////                .addContent(ContentType.LOBBY_PARTICIPANT, lobby.getHost().getName());
////        lobby.addUser(this);
////
//////        broadcast(lobby.getUsers(), "/queue/play/join", m);
////    }

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
