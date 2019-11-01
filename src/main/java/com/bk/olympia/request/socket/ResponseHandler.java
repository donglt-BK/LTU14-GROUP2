package com.bk.olympia.request.socket;

public interface ResponseHandler {
    public void success(Object response);
    public void error(String errorMessage);
}
