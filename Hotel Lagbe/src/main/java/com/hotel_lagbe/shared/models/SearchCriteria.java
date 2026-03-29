package com.hotel_lagbe.shared.models;

import java.time.LocalDate;
import java.util.List;

public class SearchCriteria {
    private String location;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private List<String> travelTypes; // e.g., ["Families", "Couples"]

    public SearchCriteria(String location, LocalDate checkInDate, LocalDate checkOutDate, List<String> travelTypes) {
        this.location = location;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.travelTypes = travelTypes;
    }

    // Getters
    public String getLocation() { return location; }
    public LocalDate getCheckInDate() { return checkInDate; }
    public LocalDate getCheckOutDate() { return checkOutDate; }
    public List<String> getTravelTypes() { return travelTypes; }

    // Setters if needed
    public void setLocation(String location) { this.location = location; }
    public void setCheckInDate(LocalDate checkInDate) { this.checkInDate = checkInDate; }
    public void setCheckOutDate(LocalDate checkOutDate) { this.checkOutDate = checkOutDate; }
    public void setTravelTypes(List<String> travelTypes) { this.travelTypes = travelTypes; }
}
