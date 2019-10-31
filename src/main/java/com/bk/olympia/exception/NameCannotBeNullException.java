package com.bk.olympia.exception;

import com.bk.olympia.base.BaseRuntimeException;

public class NameCannotBeNullException extends BaseRuntimeException {
    public NameCannotBeNullException() {
        super("Name is null.");
    }
}
