package com.hotel_lagbe.server.database;

import com.hotel_lagbe.shared.models.Booking;
import com.hotel_lagbe.shared.models.Hotel;
import com.hotel_lagbe.shared.models.StandardRoom;
import com.hotel_lagbe.shared.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStore {

    // We use a Map for users so we can instantly look them up by their username
    private Map<String, User> users;

    // Our platform now holds a List of Hotels, instead of just loose rooms!
    private List<Hotel> hotels;
    private List<Booking> bookings;

    public DataStore() {
        this.users = new HashMap<>();
        this.hotels = new ArrayList<>();
        this.bookings = new ArrayList<>();

        initializeDummyData();
    }

    private void initializeDummyData() {
        // 1. Default Users
        users.put("admin", new User("admin", "admin123", "ADMIN"));
        users.put("rupom_hazra121", new User("rupom_hazra121", "password123", "GUEST"));

        // 2. Create Hotels for the platform
        Hotel resort = new Hotel("H01", "Grand Sultan Tea Resort", "Sreemangal");
        Hotel cityHotel = new Hotel("H02", "InterContinental", "Dhaka");

        // 3. Add Rooms to those specific Hotels
        resort.addRoom(new StandardRoom("101", 5000.0, 1));
        resort.addRoom(new StandardRoom("102", 7500.0, 2));

        cityHotel.addRoom(new StandardRoom("301", 8000.0, 1));
        cityHotel.addRoom(new StandardRoom("302", 12000.0, 2));

        // 4. Add the Hotels to our platform's master list
        hotels.add(resort);
        hotels.add(cityHotel);
    }

    // --- Methods for the Server to interact with the data ---

    // Used for LOGIN
    public User getUser(String username) {
        return users.get(username);
    }

    // Used for SIGN_UP (Thread-safe to prevent two people taking the same username at the exact same time)
    public synchronized boolean addUser(User newUser) {
        if (users.containsKey(newUser.getUsername())) {
            return false; // Registration fails: Username already exists!
        }
        users.put(newUser.getUsername(), newUser);
        return true; // Registration successful
    }

    // Used for SEARCH_ROOMS
    public List<Hotel> getAllHotels() {
        return hotels;
    }

    // Used for BOOK_ROOM
    public synchronized void addBooking(Booking booking) {
        bookings.add(booking);
        booking.getBookedRoom().setBooked(true);
    }
}