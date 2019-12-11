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
    private String lobbyParticipant;

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

    public void setLobby(boolean isAlpha, String currentLobbyId, String lobbyName, String lobbyParticipant) {
        this.currentLobbyId = currentLobbyId;
        this.lobbyName = lobbyName;
        this.lobbyParticipant = lobbyParticipant;
        this.isAlpha = isAlpha;
    }

    public int getCurrentLobbyId() {
        if (currentLobbyId == null) currentLobbyId = "-1";
        return Integer.parseInt(currentLobbyId);
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public String getLobbyParticipant() {
        return lobbyParticipant;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(Double roomId) {
        this.roomId = String.valueOf(roomId.intValue());
        System.out.println("Room: " + this.roomId);
    }

    public void resetLobby() {
        this.currentLobbyId = "-1";
        this.lobbyName = "";
        this.lobbyParticipant = "";
    }
}
