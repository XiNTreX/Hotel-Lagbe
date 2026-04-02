package com.hotel_lagbe.server;

import com.hotel_lagbe.server.database.DataStore;
import com.hotel_lagbe.server.services.AuthManager;
import com.hotel_lagbe.server.services.BookingManager;
import com.hotel_lagbe.shared.models.Booking;
import com.hotel_lagbe.shared.models.User;
import com.hotel_lagbe.shared.network.Request;
import com.hotel_lagbe.shared.network.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    // The worker needs its tools
    private AuthManager authManager;
    private BookingManager bookingManager;

    // We updated the constructor to accept the DataStore
    public ClientHandler(Socket socket, DataStore dataStore) {
        this.socket = socket;
        // Create the managers and give them the database
        this.authManager = new AuthManager(dataStore);
        this.bookingManager = new BookingManager(dataStore);
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            while (true) {
                // 1. Receive the Envelope
                Request request = (Request) in.readObject();
                Response response = null;

                // 2. Read the Label and Route it
                switch (request.getType()) {
                    case LOGIN:
                        User loginUser = (User) request.getPayload();
                        response = authManager.handleLogin(loginUser);
                        break;

                    case SIGN_UP:
                        User newUser = (User) request.getPayload();
                        response = authManager.handleSignUp(newUser);
                        break;

                    case BOOK_ROOM:
                        Booking newBooking = (Booking) request.getPayload();
                        response = bookingManager.handleBooking(newBooking);
                        break;
                    case GET_MY_BOOKINGS:
                        String requestingUser = (String) request.getPayload();
                        response = bookingManager.handleGetMyBookings(requestingUser);
                        break;
                    case CANCEL_BOOKING:
                        String cancelId = (String) request.getPayload();
                        response = bookingManager.handleCancelBooking(cancelId);
                        break;
                    default:
                        response = new Response(false, "Unknown request type.");
                        break;
                }

                // 3. Send the Result back to the JavaFX screen
                out.writeObject(response);
                out.flush();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Client disconnected.");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}