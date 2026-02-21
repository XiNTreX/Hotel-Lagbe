package com.hotel_lagbe.shared.network;

import java.io.Serializable;

public enum MessageType implements Serializable {
    LOGIN,
    SIGN_UP,
    LOGOUT,
    SEARCH_ROOMS,
    BOOK_ROOM,
    CHAT_MESSAGE,

    SUCCESS,
    ERROR,
    ROOM_LIST_UPDATE
}