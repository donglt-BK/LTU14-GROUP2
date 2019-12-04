package com.bk.olympia.message;

import com.bk.olympia.model.UserSession;
import com.bk.olympia.type.ContentType;
import com.bk.olympia.type.MessageType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.Map;

@JsonDeserialize
public class Message {
    private MessageType type;
    private int sender;
    private Map<ContentType, Object> content;

    public Message() {
    }

    public Message(MessageType type) {
        this.type = type;
        this.sender = UserSession.getInstance().getUserId();
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
