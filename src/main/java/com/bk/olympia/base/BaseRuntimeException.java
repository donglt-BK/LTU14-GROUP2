package com.bk.olympia.base;

import org.apache.commons.text.StringSubstitutor;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseRuntimeException extends RuntimeException {
    private static final String TEMPLATE = "Invalid attempt: ${error}." +
            "Caused by: User ${userId}.";
    private static StringSubstitutor substitutor;
    private int userId;

    public BaseRuntimeException(String error, int userId) {
        super(pack(error, userId));
    }

    private static String pack(String error, int userId) {
        Map<String, String> values = new HashMap<>();
        values.put("error", error);
        values.put("userId", String.valueOf(userId));
        substitutor = new StringSubstitutor(values);
        return substitutor.replace(TEMPLATE);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
