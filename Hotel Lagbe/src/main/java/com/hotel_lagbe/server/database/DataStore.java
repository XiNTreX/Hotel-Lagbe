package com.hotel_lagbe.server.database;

import com.hotel_lagbe.shared.models.User;

import java.util.HashMap;
import java.util.Map;

public class DataStore {
    private java.util.List<com.hotel_lagbe.shared.models.Booking> bookings;
    private Map<String, User> users;

    public DataStore() {
        this.users = new HashMap<>();
        this.bookings = new java.util.ArrayList<>();
        users.put("admin", new User("Admin User", "admin", "admin123"));
    }

    public User getUser(String username) {
        return users.get(username);
    }

    public synchronized boolean addUser(User newUser) {
        if (users.containsKey(newUser.getUsername())) {
            return false;
        }

        users.put(newUser.getUsername(), newUser);
        return true;
    }
    public synchronized boolean addBooking(com.hotel_lagbe.shared.models.Booking booking) {
        bookings.add(booking);
        return true;
    }
    public synchronized java.util.List<com.hotel_lagbe.shared.models.Booking> getBookingsForUser(String username) {
        java.util.List<com.hotel_lagbe.shared.models.Booking> userBookings = new java.util.ArrayList<>();
        for (com.hotel_lagbe.shared.models.Booking b : bookings) {
            if (b.getGuest().getUsername().equals(username)) {
                userBookings.add(b);
            }
        }
        return userBookings;
    }
    public synchronized boolean cancelBooking(String bookingId) {
        return bookings.removeIf(booking -> booking.getBookingId().equals(bookingId));
    }
}