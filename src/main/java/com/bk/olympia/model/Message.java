package com.bk.olympia.model;

public class Message {
    private int type;
    private String sender;
    private String content;

    public Message() {
    }

    public Message(int type, String sender, String content) {
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
