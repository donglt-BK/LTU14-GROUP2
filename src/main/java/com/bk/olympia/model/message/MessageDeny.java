package com.bk.olympia.model.message;

import com.bk.olympia.constant.ContentType;
import com.bk.olympia.constant.MessageType;

public class MessageDeny extends Message {
    public MessageDeny(MessageType type, int sender) {
        super(type, sender);
        addContent(ContentType.STATUS, false);
    }
}
