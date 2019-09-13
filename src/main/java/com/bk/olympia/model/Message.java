package com.bk.olympia.model;

import java.util.HashMap;

public class Message {
    private int type;
    private String sender;
    private HashMap content;

    public Message() {
    }

    public Message(int type, String sender, HashMap content) {
        this.type = type;
        this.sender = sender;
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public HashMap getContent() {
        return content;
    }

    public void setContent(HashMap content) {
        this.content = content;
    }
}
