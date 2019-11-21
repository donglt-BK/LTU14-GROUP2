package com.bk.olympia.exception;

import com.bk.olympia.base.BaseRuntimeException;
import com.bk.olympia.constant.ErrorType;

public class UsernameAlreadyTakenException extends BaseRuntimeException {
    public UsernameAlreadyTakenException() {
        super("Username can't be used for register. It is already taken.", ErrorType.DUPLICATE_USERNAME);
    }
}
