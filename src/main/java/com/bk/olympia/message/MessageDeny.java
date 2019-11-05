package com.bk.olympia.message;

import com.bk.olympia.type.ContentType;
import com.bk.olympia.type.MessageType;

public class MessageDeny extends Message {
    public MessageDeny(MessageType type, int sender) {
        super(type, sender);
        addContent(ContentType.STATUS, false);
    }
}
