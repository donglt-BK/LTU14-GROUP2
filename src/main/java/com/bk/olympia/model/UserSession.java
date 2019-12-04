package com.bk.olympia.model;

import com.bk.olympia.type.ContentType;

import java.util.Map;

public class UserSession {
    private int userId = -1;

    private String name;
    private String username;
    private int balance;

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
        balance = (int) content.get(ContentType.BALANCE);
        name = (String) content.get(ContentType.NAME);
        username = (String) content.get(ContentType.USERNAME);
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
}
