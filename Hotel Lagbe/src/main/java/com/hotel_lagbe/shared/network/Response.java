package com.hotel_lagbe.shared.network;

import java.io.Serializable;

public class Response implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean isSuccess;
    private String message;
    private Object payload;

    public Response(boolean isSuccess, String message, Object payload) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.payload = payload;
    }

    // Constructor for simple success/error messages without extra data
    public Response(boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.payload = null;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public Object getPayload() {
        return payload;
    }
}