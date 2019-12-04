package com.bk.olympia.model;

public class UserSession {
    private int userId = -1;

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

    public void setUserId(int userId) {
        System.out.println("Update user id: " + userId);
        this.userId = userId;
    }
}
