package com.bk.olympia.exception;

import com.bk.olympia.base.BaseRuntimeException;

public class InsufficientBalanceException extends BaseRuntimeException {
    public InsufficientBalanceException(int userId) {
        super("Insufficient balance", userId);
    }
}
