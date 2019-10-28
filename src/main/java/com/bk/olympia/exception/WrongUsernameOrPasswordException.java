package com.bk.olympia.exception;

import com.bk.olympia.base.BaseRuntimeException;

public class WrongUsernameOrPasswordException extends BaseRuntimeException {
    public WrongUsernameOrPasswordException(int userId) {
        super("Wrong Username or Password", userId);
    }
}
