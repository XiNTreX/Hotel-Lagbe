package com.hotel_lagbe.server.services;

import com.hotel_lagbe.server.database.DataStore;
import com.hotel_lagbe.shared.models.User;
import com.hotel_lagbe.shared.network.Response;

public class AuthManager {

    private DataStore dataStore;

    // The AuthManager needs the DataStore to do its job
    public AuthManager(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    // --- Handling Login Logic ---
    public Response handleLogin(User loginAttempt) {
        User existingUser = dataStore.getUser(loginAttempt.getUsername());

        if (existingUser == null) {
            return new Response(false, "Username not found. Please sign up.");
        }

        // Compare the password sent by the client with the one in our database
        if (existingUser.getPassword().equals(loginAttempt.getPassword())) {
            return new Response(true, "Welcome back, " + existingUser.getFullName() + "!");
        } else {
            return new Response(false, "Incorrect password.");
        }
    }

    // --- Handling Sign-Up Logic ---
    public Response handleSignUp(User newUser) {
        boolean isSaved = dataStore.addUser(newUser);

        if (isSaved) {
            return new Response(true, "Account created! You can now log in.");
        } else {
            return new Response(false, "That username is already taken.");
        }
    }
}