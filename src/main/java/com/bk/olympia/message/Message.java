package com.bk.olympia.message;

import java.util.HashMap;

public class Message {
    private MessageType type;
    private int sender;
    private HashMap content;

    public Message(MessageType type) {
        this.type = type;
        this.sender = 0;
        content = new HashMap();
    }

    public Message(MessageType type, int sender) {
        this.type = type;
        this.sender = sender;
        content = new HashMap();
    }

    public Message(MessageType type, int sender, HashMap content) {
        this.type = type;
        this.sender = sender;
        this.content = content;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public HashMap getContent() {
        return content;
    }

    public <T> T getContent(String type) {
        return (T) content.get(type);
    }

    public void setContent(HashMap content) {
        this.content = content;
    }

    public <T> Message addContent(String type, T detail) {
        this.content.put(type, detail);
        return this;
    }

    public void pack() {

    }
}
