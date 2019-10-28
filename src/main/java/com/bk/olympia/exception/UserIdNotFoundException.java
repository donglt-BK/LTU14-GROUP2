package com.bk.olympia.exception;

import com.bk.olympia.base.BaseRuntimeException;

public class UserIdNotFoundException extends BaseRuntimeException {
    public UserIdNotFoundException(int userId) {
        super("User ID not found", userId);
    }
}
