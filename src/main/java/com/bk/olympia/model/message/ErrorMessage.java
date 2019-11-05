package com.bk.olympia.model.message;

import com.bk.olympia.model.type.ErrorType;
import com.bk.olympia.model.type.MessageType;

public class ErrorMessage extends Message {
    private ErrorType errorType;

    public ErrorMessage(ErrorType errorType, int sender) {
        super(MessageType.ERROR, sender);
        this.errorType = errorType;
    }

    public void setErrorType(ErrorType errorType) {
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
