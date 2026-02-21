package com.hotel_lagbe.shared.models;

import java.io.Serializable;

public abstract class Room implements Serializable {
    private static final long serialVersionUID = 1L;

    // 'protected' access protection: visible to subclasses and the 'models' package
    protected String roomNumber;
    protected double pricePerNight;
    protected boolean isBooked;

    public Room(String roomNumber, double pricePerNight) {
        this.roomNumber = roomNumber;
        this.pricePerNight = pricePerNight;
        this.isBooked = false;
    }

    // Abstract method: Forces all child classes to provide their own implementation
    public abstract String getRoomDetails();

    // Standard Getters and Setters
    public String getRoomNumber() { return roomNumber; }
    public double getPricePerNight() { return pricePerNight; }
    public boolean isBooked() { return isBooked; }
    public void setBooked(boolean booked) { isBooked = booked; }
}