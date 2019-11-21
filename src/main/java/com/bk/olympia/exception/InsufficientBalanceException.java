package com.bk.olympia.exception;

import com.bk.olympia.base.BaseRuntimeException;
import com.bk.olympia.constant.ErrorType;

public class InsufficientBalanceException extends BaseRuntimeException {
    public InsufficientBalanceException(int userId) {
        super("Insufficient balance", userId, ErrorType.INSUFFICIENT_BALANCE);
    }
}
