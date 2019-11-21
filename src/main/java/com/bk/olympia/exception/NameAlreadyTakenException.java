package com.bk.olympia.exception;

import com.bk.olympia.base.BaseRuntimeException;
import com.bk.olympia.constant.ErrorType;

public class NameAlreadyTakenException extends BaseRuntimeException {
    public NameAlreadyTakenException() {
        super("Name is already taken.", -1, ErrorType.DUPLICATE_NAME);
    }
}
