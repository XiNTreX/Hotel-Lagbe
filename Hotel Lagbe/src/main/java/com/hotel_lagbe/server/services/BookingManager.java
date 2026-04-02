package com.hotel_lagbe.server.services;

import com.hotel_lagbe.server.database.DataStore;
import com.hotel_lagbe.shared.models.Booking;
import com.hotel_lagbe.shared.network.Response;

public class BookingManager {
    private DataStore dataStore;

    public BookingManager(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public Response handleBooking(Booking newBooking) {
        boolean isSaved = dataStore.addBooking(newBooking);

        if (isSaved) {
            System.out.println("New booking saved for: " + newBooking.getGuest().getUsername());
            return new Response(true, "Booking confirmed successfully!");
        } else {
            return new Response(false, "Failed to save booking.");
        }
    }
    public Response handleGetMyBookings(String username) {
        java.util.List<Booking> myBookings = dataStore.getBookingsForUser(username);
        return new Response(true, "Bookings retrieved", myBookings);
    }
    public Response handleCancelBooking(String bookingId) {
        boolean isRemoved = dataStore.cancelBooking(bookingId);
        if (isRemoved) {
            System.out.println("Booking cancelled: " + bookingId);
            return new Response(true, "Booking cancelled successfully.");
        } else {
            return new Response(false, "Could not find booking to cancel.");
        }
    }
}