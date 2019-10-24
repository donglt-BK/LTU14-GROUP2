package com.bk.olympia.model.message;

import com.bk.olympia.model.type.ContentType;
import com.bk.olympia.model.type.MessageType;

public class MessageDeny extends Message {
    public MessageDeny(MessageType type, int sender) {
        super(type, sender);
        addContent(ContentType.STATUS, false);
    }
}
