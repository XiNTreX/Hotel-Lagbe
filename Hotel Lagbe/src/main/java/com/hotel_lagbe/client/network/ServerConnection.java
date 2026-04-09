package com.hotel_lagbe.client.network;

import com.hotel_lagbe.shared.network.Request;
import com.hotel_lagbe.shared.network.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerConnection {
    private static final String HOST = "localhost";
    private static final int PORT = 8080;

    private static ServerConnection instance;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private ServerConnection() {
        connect();
    }

    public static ServerConnection getInstance() {
        if (instance == null) {
            instance = new ServerConnection();
        }
        return instance;
    }

    private void connect() {
        try {
            socket = new Socket(HOST, PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println("Successfully connected to the Server!");
        } catch (IOException e) {
            System.err.println("Failed to connect to server. Is ServerMain running?");
        }
    }

    public Response sendRequest(Request request) {
        try {
            out.writeObject(request);
            out.flush();
            return (Response) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new Response(false, "Network connection lost.");
        }
    }
}