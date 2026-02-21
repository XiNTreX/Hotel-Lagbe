package com.hotel_lagbe.shared.network;

import java.io.Serializable;

public class Request implements Serializable {
    private static final long serialVersionUID = 1L;

    private MessageType type;
    private Object payload;

    public Request(MessageType type, Object payload) {
        this.type = type;
        this.payload = payload;
    }

    public Request(MessageType type) {
        this.type = type;
        this.payload = null;
    }

    public MessageType getType() {
        return type;
    }

    public Object getPayload() {
        return payload;
    }
}