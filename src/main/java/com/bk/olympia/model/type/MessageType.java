package com.bk.olympia.model.type;

public enum MessageType {
    LOGIN(1),
    QUEUE(2);

    private int value;

    MessageType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
