package com.bk.olympia.base;

import org.apache.commons.text.StringSubstitutor;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseRuntimeException extends RuntimeException {
    private static final String TEMPLATE = "Invalid attempt: ${error}. " +
            "Caused by: User ${userId}.";
    private int userId;

    public BaseRuntimeException(String error) {
        super(pack(error, -1));
    }

    public BaseRuntimeException(String error, int userId) {
        super(pack(error, userId));
        this.userId = userId;
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

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
