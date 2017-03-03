package server;



import logic.NetFrame;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server implements Runnable {

    private static final int PORT = 6934;

    private ServerSocket serverSocket;
    private Controller controller;
    private ArrayList<Connection> connections;
    private boolean running;

    public Server() {

        running = false;
        connections = new ArrayList<>();

        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Controller getController() {
        return controller;
    }

    public void run() {

        if (running) return;

        System.out.println("Starting server. Waiting connection...");

        running = true;

        while (running) {

            Socket socket = null;

            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (socket != null) {

                Connection connection = new Connection(socket, this);
                Thread connectionThread = new Thread(connection);
                connectionThread.start();

                connections.add(connection);
                System.out.println("Connection successful with " + socket.getInetAddress().getHostName());

            }

        }


    }

    public void close() {

        if (running) {

            for (Connection connection : connections) {
                connection.close();
            }

            connections.clear();

        }

        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    public void sendFrame(NetFrame frame) {

        for (Connection connection : connections) {
            connection.sendFrame(frame);
        }

    }

}