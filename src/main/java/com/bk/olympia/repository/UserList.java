package com.bk.olympia.repository;

import com.bk.olympia.model.entity.User;

import java.util.ArrayList;
import java.util.HashMap;

public class UserList {
    private static HashMap<Integer, ArrayList<User>> usersList = new HashMap<>();

    public static void addRoom(int id, ArrayList<User> users) {
        usersList.put(id, users);
    }

    public static ArrayList<User> getUsers(int id) {
        return usersList.get(id);
    }

    public static User getUserByPosition(int id, int pos) {
        return usersList.get(id).get(pos);
    }
}
