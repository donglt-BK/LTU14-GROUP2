package com.bk.olympia.request.socket;

import com.bk.olympia.message.ErrorMessage;
import com.bk.olympia.message.Message;

public interface OldResponseHandler {
    public void success(Message responseMesage);
    public void error(ErrorMessage errorMessage);
}
