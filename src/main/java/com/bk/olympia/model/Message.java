package com.bk.olympia.model;

import com.bk.olympia.base.Broadcaster;
import com.bk.olympia.model.type.MessageType;

import java.util.HashMap;

public class Message extends Broadcaster {
    private MessageType type;
    private int sender;
    private HashMap content;

    public Message() {
    }

    public Message(MessageType type, int sender) {
        this.type = type;
        this.sender = sender;
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
}
