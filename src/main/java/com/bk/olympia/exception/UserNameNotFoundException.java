package com.bk.olympia.exception;

import com.bk.olympia.base.BaseRuntimeException;

public class UserNameNotFoundException extends BaseRuntimeException {
    public UserNameNotFoundException(int userId) {
        super("User ID not found", userId);
    }
}
