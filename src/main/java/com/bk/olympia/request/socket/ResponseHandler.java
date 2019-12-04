package com.bk.olympia.request.socket;

import com.bk.olympia.message.ErrorMessage;
import com.bk.olympia.message.Message;

public interface ResponseHandler {
    public void handle(Message response);
}
