package com.hotel_lagbe.server;

import com.hotel_lagbe.server.database.DataStore;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        System.out.println("Starting Hotel-Lagbe Server on port " + PORT + "...");

        DataStore database = new DataStore();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is online and waiting for clients!");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());

                ClientHandler handler = new ClientHandler(clientSocket, database);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
}