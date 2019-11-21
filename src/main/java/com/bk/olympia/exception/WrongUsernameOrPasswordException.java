package com.bk.olympia.exception;

import com.bk.olympia.base.BaseRuntimeException;
import com.bk.olympia.constant.ErrorType;

public class WrongUsernameOrPasswordException extends BaseRuntimeException {
    public WrongUsernameOrPasswordException() {
        super("Wrong Username or Password", ErrorType.AUTHENTICATION);
    }
}
