package com.hotel_lagbe.shared.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID; // Add this import

public class Booking implements Serializable {
    private static final long serialVersionUID = 1L;

    private String bookingId; // Add this new field
    private User guest;
    private Room bookedRoom;
    private LocalDateTime bookingTime;
    private String hotelName;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double totalPrice;

    public Booking(User guest, Room bookedRoom, String hotelName, LocalDate checkInDate, LocalDate checkOutDate, double totalPrice) {
        this.bookingId = UUID.randomUUID().toString(); // Generate unique ID
        this.guest = guest;
        this.bookedRoom = bookedRoom;
        this.bookingTime = LocalDateTime.now();
        this.hotelName = hotelName;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalPrice = totalPrice;
    }

    // Add this new Getter
    public String getBookingId() { return bookingId; }

    // ... (Keep existing getters) ...
    public User getGuest() { return guest; }
    public Room getBookedRoom() { return bookedRoom; }
    public LocalDateTime getBookingTime() { return bookingTime; }
    public String getHotelName() { return hotelName; }
    public LocalDate getCheckInDate() { return checkInDate; }
    public LocalDate getCheckOutDate() { return checkOutDate; }
    public double getTotalPrice() { return totalPrice; }
}