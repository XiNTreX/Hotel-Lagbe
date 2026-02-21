package com.hotel_lagbe.shared.models;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Booking implements Serializable {
    private static final long serialVersionUID = 1L;

    private User guest;
    private Room bookedRoom;
    private LocalDateTime bookingTime;

    public Booking(User guest, Room bookedRoom) {
        this.guest = guest;
        this.bookedRoom = bookedRoom;
        this.bookingTime = LocalDateTime.now();
    }

    public User getGuest() { return guest; }
    public Room getBookedRoom() { return bookedRoom; }
    public LocalDateTime getBookingTime() { return bookingTime; }
}