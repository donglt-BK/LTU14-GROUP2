package com.bk.olympia.model.message;

import com.bk.olympia.model.type.ContentType;
import com.bk.olympia.model.type.MessageType;

import java.util.HashMap;
import java.util.Map;

public class Message {
    private MessageType type;
    protected int sender;
    private Map<ContentType, Object> content;

    public Message() {
    }

    public Message(MessageType type) {
        this.type = type;
        this.sender = -1;
        content = new HashMap<>();
    }

    public Message(MessageType type, int sender) {
        this.type = type;
        this.sender = sender;
        content = new HashMap<>();
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

    public <T> T getContent(ContentType type) {
        return (T) (content.get(type));
    }

    public Map getContent() {
        return content;
    }

    public <T> Message addContent(ContentType type, T detail) {
        this.content.put(type, detail);
        return this;
    }

    //TODO: Hàm encrypt gói tin
    public Message pack() {
        return this;
    }
}
