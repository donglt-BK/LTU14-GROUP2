package com.bk.olympia.message;

public class ErrorMessage extends Message {
    private ErrorType type;

    public ErrorMessage(ErrorType type, int sender) {
        this.type = type;
        this.setSender(sender);
    }

    public void setType(ErrorType type) {
        this.type = type;
    }
}
