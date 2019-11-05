package com.bk.olympia.message;


import com.bk.olympia.type.ErrorType;

public class ErrorMessage extends Message {
    private ErrorType errorType;



    public ErrorMessage(ErrorType errorType, int sender) {
        this.errorType = errorType;
        this.setSender(sender);
    }

    public void setType(ErrorType errorType) {
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
