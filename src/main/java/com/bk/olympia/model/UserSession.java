package com.bk.olympia.model;

public class UserSession {
    private static UserSession instance;
    public static UserSession getInstance(){
        if(instance == null){
            instance = new UserSession();
        }
        return instance;
    }
}
