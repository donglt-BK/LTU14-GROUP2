package com.bk.olympia.model;

import com.bk.olympia.type.ContentType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserSession {
    private int userId = -1;

    private String name;
    private String username;
    private int balance;
    private boolean isAlpha = false;
    private String currentLobbyId;
    private String lobbyName;
    private List<String> lobbyParticipant;

    private String roomId;

    private static UserSession instance;
    public static UserSession getInstance(){
        if(instance == null){
            instance = new UserSession();
        }
        return instance;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(Double userId) {
        System.out.println("Update user id: " + userId);
        this.userId = userId.intValue();
    }

    public void setData(Map content) {
        balance = ((Double) content.get(ContentType.BALANCE)).intValue();
        name = (String) content.get(ContentType.NAME);
        username = (String) content.get(ContentType.USERNAME);
        System.out.println(toString());
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public int getBalance() {
        return balance;
    }

    public boolean isAlpha() {
        return isAlpha;
    }

    public void setAlpha(boolean alpha) {
        isAlpha = alpha;
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", balance=" + balance +
                '}';
    }

    public void setLobby(String currentLobbyId, String lobbyName, List<String> lobbyParticipant) {
        this.currentLobbyId = currentLobbyId;
        this.lobbyName = lobbyName;
        this.lobbyParticipant = lobbyParticipant;
    }

    public String getCurrentLobbyId() {
        return currentLobbyId;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public List<String> getLobbyParticipant() {
        return lobbyParticipant;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public List<String> setLobbyParticipant(List<String> lobbyParticipant) {
        List<String> newParticipant = new ArrayList<>();
        lobbyParticipant.stream().filter(participant -> this.lobbyParticipant.contains(participant)).forEach(newParticipant::add);
        this.lobbyParticipant.addAll(newParticipant);
        return newParticipant;
    }
}
