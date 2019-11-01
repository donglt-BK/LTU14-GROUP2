package com.bk.olympia.exception;

import com.bk.olympia.base.BaseRuntimeException;

public class UnauthorizedActionException extends BaseRuntimeException {
    public UnauthorizedActionException(int userId) {
        super("No authority to perform this action", userId);
    }
}
