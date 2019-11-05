package com.bk.olympia.message;

import com.bk.olympia.type.ContentType;
import com.bk.olympia.type.MessageType;

public class MessageAccept extends Message {
    public MessageAccept(MessageType type, int sender) {
        super(type, sender);
        addContent(ContentType.STATUS, true);
    }
}
