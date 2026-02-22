package com.hotel_lagbe.shared.models;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String fullName; // NEW
    private String username;
    private String password;

    // Constructor for Signing Up (Needs all three)
    public User(String fullName, String username, String password) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
    }

    // Constructor for Logging In (Only needs username and password)
    public User(String username, String password) {
        this.fullName = "";
        this.username = username;
        this.password = password;
    }

    public String getFullName() { return fullName; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
}