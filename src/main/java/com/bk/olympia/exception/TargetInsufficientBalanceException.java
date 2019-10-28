package com.bk.olympia.exception;

import com.bk.olympia.base.BaseRuntimeException;

public class TargetInsufficientBalanceException extends BaseRuntimeException {
    public TargetInsufficientBalanceException(int userId) {
        super("Invited Target has insufficient balance", userId);
    }
}
