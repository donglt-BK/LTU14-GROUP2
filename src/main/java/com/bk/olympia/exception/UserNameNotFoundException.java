package com.bk.olympia.exception;

import com.bk.olympia.base.BaseRuntimeException;
import com.bk.olympia.constant.ErrorType;

public class UserNameNotFoundException extends BaseRuntimeException {
    public UserNameNotFoundException(int userId) {
        super("User ID not found", userId, ErrorType.WRONG_NAME);
    }
}
