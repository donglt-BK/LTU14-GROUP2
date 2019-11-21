package com.bk.olympia.exception;

import com.bk.olympia.base.BaseRuntimeException;
import com.bk.olympia.constant.ErrorType;

public class PasswordCannotBeNullException extends BaseRuntimeException {
    public PasswordCannotBeNullException() {
        super("Password is null.", ErrorType.PASSWORD_IS_NULL);
    }
}
