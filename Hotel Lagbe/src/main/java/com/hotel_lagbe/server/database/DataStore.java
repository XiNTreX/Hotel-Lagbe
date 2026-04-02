package com.hotel_lagbe.server.database;

import com.hotel_lagbe.shared.models.User;

import java.util.HashMap;
import java.util.Map;

public class DataStore {
    private java.util.List<com.hotel_lagbe.shared.models.Booking> bookings;
    // The Map storing all our registered users. Key = username, Value = User object
    private Map<String, User> users;

    public DataStore() {
        this.users = new HashMap<>();
        this.bookings = new java.util.ArrayList<>();
        // Let's add a dummy admin account so you can test logging in immediately
        users.put("admin", new User("Admin User", "admin", "admin123"));
    }

    // Retrieves a user by their username. Returns null if they don't exist.
    public User getUser(String username) {
        return users.get(username);
    }

    // Adds a new user. The 'synchronized' keyword ensures that if two people
    // try to sign up at the exact same millisecond, the server won't crash.
    public synchronized boolean addUser(User newUser) {
        // Check if the username is already taken
        if (users.containsKey(newUser.getUsername())) {
            return false;
        }

        // Save the new user to our Map
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
        // removeIf returns true if it successfully found and removed the item
        return bookings.removeIf(booking -> booking.getBookingId().equals(bookingId));
    }
}