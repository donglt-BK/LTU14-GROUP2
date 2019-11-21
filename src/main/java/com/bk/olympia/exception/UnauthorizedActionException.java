package com.bk.olympia.exception;

import com.bk.olympia.base.BaseRuntimeException;
import com.bk.olympia.constant.ErrorType;

public class UnauthorizedActionException extends BaseRuntimeException {
    public UnauthorizedActionException(int userId) {
        super("No authority to perform this action", userId, ErrorType.INVALID_ACTION);
    }
}
