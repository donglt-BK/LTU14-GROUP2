package com.bk.olympia.exception;

import com.bk.olympia.base.BaseRuntimeException;
import com.bk.olympia.constant.ErrorType;

public class TargetInsufficientBalanceException extends BaseRuntimeException {
    public TargetInsufficientBalanceException(int userId) {
        super("Invited Target has insufficient balance", userId, ErrorType.TARGET_INSUFFICIENT_BALANCE);
    }
}
