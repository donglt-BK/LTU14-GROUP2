package com.bk.olympia.message;

import java.util.HashMap;
import java.util.Map;

public class Message {
    private MessageType type;
    private int sender;
    private Map content;

    public Message() {
    }

    public Message(MessageType type) {
        this.type = type;
        this.sender = 0;
        this.content = new HashMap();
    }

    public Message(MessageType type, int sender) {
        this.type = type;
        this.sender = sender;
        this.content = new HashMap();
    }

    public Message(MessageType type, int sender, Map content) {
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

    public Map getContent() {
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
}
