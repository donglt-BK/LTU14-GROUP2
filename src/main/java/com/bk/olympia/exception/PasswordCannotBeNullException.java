package com.bk.olympia.exception;

import com.bk.olympia.base.BaseRuntimeException;

public class PasswordCannotBeNullException extends BaseRuntimeException {
    public PasswordCannotBeNullException() {
        super("Password is null.");
    }
}
