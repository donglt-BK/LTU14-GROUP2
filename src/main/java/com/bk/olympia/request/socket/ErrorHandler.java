package com.bk.olympia.request.socket;

import com.bk.olympia.message.ErrorMessage;

public interface ErrorHandler {
    public void handle(ErrorMessage response);
}
