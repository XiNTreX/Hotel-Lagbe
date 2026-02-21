package com.hotel_lagbe.shared.models;

public class StandardRoom extends Room {
    private static final long serialVersionUID = 1L;

    private int numberOfBeds;

    public StandardRoom(String roomNumber, double pricePerNight, int numberOfBeds) {
        super(roomNumber, pricePerNight);
        this.numberOfBeds = numberOfBeds;
    }

    @Override
    public String getRoomDetails() {
        return "Standard Room with " + numberOfBeds + " bed(s).";
    }

    public int getNumberOfBeds() { return numberOfBeds; }
}