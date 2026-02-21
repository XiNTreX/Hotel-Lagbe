package com.hotel_lagbe.server.services;

import com.hotel_lagbe.server.database.DataStore;
import com.hotel_lagbe.shared.models.User;
import com.hotel_lagbe.shared.network.Response;

public class AuthManager {

    private DataStore dataStore;

    public AuthManager(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public Response handleLogin(User loginAttempt) {
        User existingUser = dataStore.getUser(loginAttempt.getUsername());

        if (existingUser == null) {
            return new Response(false, "Error: User does not exist. Please sign up.");
        }

        if (existingUser.getPassword().equals(loginAttempt.getPassword())) {
            return new Response(true, "Login successful! Welcome back, " + existingUser.getUsername(), existingUser);
        } else {
            return new Response(false, "Error: Incorrect password.");
        }
    }

    public Response handleSignUp(User newUser) {
        boolean isRegistered = dataStore.addUser(newUser);

        if (isRegistered) {
            return new Response(true, "Account created successfully! You can now log in.");
        } else {
            return new Response(false, "Error: Username is already taken. Please choose another.");
        }
    }
}