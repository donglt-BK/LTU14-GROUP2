package com.bk.olympia.model.type;

public enum MessageType {
    LOGIN(1),
    LOGOUT(2),
    QUEUE(3);

    private int value;

    MessageType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
