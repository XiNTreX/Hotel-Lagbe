package com.hotel_lagbe.server.services;

import com.hotel_lagbe.server.database.DataStore;
import com.hotel_lagbe.shared.models.User;
import com.hotel_lagbe.shared.network.Response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AuthManager {

    private DataStore dataStore;

    private Map<String, List<LocalDateTime>> failedAttempts;

    public AuthManager(DataStore dataStore) {
        this.dataStore = dataStore;
        this.failedAttempts = new ConcurrentHashMap<>();
    }

    // --- Handling Login Logic ---
    public Response handleLogin(User loginAttempt) {
        String username = loginAttempt.getUsername();
        User existingUser = dataStore.getUser(username);

        if (existingUser == null) {
            return new Response(false, "Username not found. Please sign up.");
        }

        cleanUpOldAttempts(username);

        List<LocalDateTime> attempts = failedAttempts.getOrDefault(username, new ArrayList<>());
        if (attempts.size() >= 3) {
            return new Response(false, "Multiple login attempted. Please wait 30 seconds.");
        }

        if (existingUser.getPassword().equals(loginAttempt.getPassword())) {
            failedAttempts.remove(username);
            return new Response(true, "Welcome back, " + existingUser.getFullName() + "!");
        } else {
            attempts.add(LocalDateTime.now());
            failedAttempts.put(username, attempts);
            return new Response(false, "Incorrect password.");
        }
    }

    private void cleanUpOldAttempts(String username) {
        List<LocalDateTime> attempts = failedAttempts.get(username);
        if (attempts != null) {
            LocalDateTime oneminuteago = LocalDateTime.now().minusSeconds(60);

            attempts.removeIf(time -> time.isBefore(oneminuteago));

            if (attempts.isEmpty()) {
                failedAttempts.remove(username);
            } else {
                failedAttempts.put(username, attempts);
            }
        }
    }

    public Response handleSignUp(User newUser) {
        boolean isSaved = dataStore.addUser(newUser);

        if (isSaved) {
            return new Response(true, "Account created! You can now log in.");
        } else {
            return new Response(false, "That username is already taken.");
        }
    }
}