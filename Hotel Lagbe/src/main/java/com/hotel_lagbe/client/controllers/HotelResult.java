package com.hotel_lagbe.shared.models;

import java.io.Serializable;

/**
 * HotelResult - Used for displaying hotels fetched from Google Places API.
 * This is separate from your original Hotel.java (which is for booking system).
 */
public class HotelResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private String placeId;
    private String name;
    private String address;
    private double rating;
    private int priceLevel;
    private boolean openNow;
    private String photoUrl;
    private double latitude;
    private double longitude;

    // Default constructor (required for serialization)
    public HotelResult() {}

    // Getters
    public String getPlaceId()    { return placeId; }
    public String getName()       { return name; }
    public String getAddress()    { return address; }
    public double getRating()     { return rating; }
    public int getPriceLevel()    { return priceLevel; }
    public boolean isOpenNow()    { return openNow; }
    public String getPhotoUrl()   { return photoUrl; }
    public double getLatitude()   { return latitude; }
    public double getLongitude()  { return longitude; }

    // Setters (used by HotelSearchService)
    public void setPlaceId(String placeId)       { this.placeId = placeId; }
    public void setName(String name)             { this.name = name; }
    public void setAddress(String address)       { this.address = address; }
    public void setRating(double rating)         { this.rating = rating; }
    public void setPriceLevel(int priceLevel)    { this.priceLevel = priceLevel; }
    public void setOpenNow(boolean openNow)      { this.openNow = openNow; }
    public void setPhotoUrl(String photoUrl)     { this.photoUrl = photoUrl; }
    public void setLatitude(double latitude)     { this.latitude = latitude; }
    public void setLongitude(double longitude)   { this.longitude = longitude; }

    @Override
    public String toString() {
        return "HotelResult{" +
                "name='" + name + '\'' +
                ", rating=" + rating +
                ", priceLevel=" + priceLevel +
                '}';
    }
}