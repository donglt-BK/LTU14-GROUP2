package com.bk.olympia.request.handler;

import com.bk.olympia.message.ErrorMessage;

public interface ErrorHandler {
    public void handle(ErrorMessage response);
}
