package com.bk.olympia.exception;

import com.bk.olympia.base.BaseRuntimeException;

public class NameAlreadyTakenException extends BaseRuntimeException {
    public NameAlreadyTakenException() {
        super("Name is already taken.", -1);
    }
}
