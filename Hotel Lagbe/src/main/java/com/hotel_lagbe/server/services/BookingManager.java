package com.hotel_lagbe.server.services;

import com.hotel_lagbe.server.database.DataStore;
import com.hotel_lagbe.shared.models.Booking;
import com.hotel_lagbe.shared.models.Room;
import com.hotel_lagbe.shared.models.User;
import com.hotel_lagbe.shared.network.Response;

import java.util.List;
import java.util.stream.Collectors;

public class BookingManager {

    private DataStore dataStore;

    public BookingManager(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public Response searchAvailableRooms(double maxPrice) {

        List<Room> availableRooms = dataStore.getAllHotels().stream()
                .flatMap(hotel -> hotel.getRooms().stream())
                .filter(room -> !room.isBooked())
                .filter(room -> room.getPricePerNight() <= maxPrice)
                .collect(Collectors.toList());

        if (availableRooms.isEmpty()) {
            return new Response(true, "No rooms found under " + maxPrice + " BDT.", availableRooms);
        }

        return new Response(true, "Found " + availableRooms.size() + " available rooms.", availableRooms);
    }

    public Response bookRoom(User guest, Room roomToBook) {
        if (roomToBook.isBooked()) {
            return new Response(false, "Sorry, this room was just booked by someone else.");
        }

        Booking newBooking = new Booking(guest, roomToBook);
        dataStore.addBooking(newBooking);

        return new Response(true, "Successfully booked Room " + roomToBook.getRoomNumber() + "!");
    }
}