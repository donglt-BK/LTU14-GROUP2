package com.bk.olympia.base;

import com.bk.olympia.constant.ErrorType;
import org.apache.commons.text.StringSubstitutor;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseRuntimeException extends RuntimeException {
    private static final String TEMPLATE = "Invalid attempt: ${error}. " +
            "Caused by: User ${userId}.";
    private final int userId;
    private final ErrorType type;

    public BaseRuntimeException(String error, ErrorType type) {
        super(pack(error, -1));
        this.userId = -1;
        this.type = type;
    }

    public BaseRuntimeException(String error, int userId, ErrorType type) {
        super(pack(error, userId));
        this.userId = userId;
        this.type = type;
    }

    private static String pack(String error, int userId) {
        Map<String, String> values = new HashMap<>();
        values.put("error", error);
        values.put("userId", String.valueOf(userId));
        StringSubstitutor substitutor = new StringSubstitutor(values);
        return substitutor.replace(TEMPLATE);
    }

    public int getUserId() {
        return userId;
    }

    public ErrorType getType() {
        return type;
    }
}
