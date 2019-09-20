package com.bk.olympia.model.type;

public enum MessageType {
    LOGIN(1),
    LOGOUT(2),
    JOIN_QUEUE(100),
    LEAVE_QUEUE(101),
    START_GAME(200),
    CREATE_ROOM(201);

    private int value;

    MessageType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
