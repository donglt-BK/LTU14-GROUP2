package com.bk.olympia.exception;

import com.bk.olympia.base.BaseRuntimeException;
import com.bk.olympia.constant.ErrorType;

public class NameCannotBeNullException extends BaseRuntimeException {
    public NameCannotBeNullException() {
        super("Name is null.", ErrorType.NAME_IS_NULL);
    }
}
