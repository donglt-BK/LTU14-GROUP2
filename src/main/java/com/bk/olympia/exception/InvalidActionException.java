package com.bk.olympia.exception;

import com.bk.olympia.base.BaseRuntimeException;

public class InvalidActionException extends BaseRuntimeException {
    public InvalidActionException(int userId) {
        super("No authority to perform this action", userId);
    }
}
