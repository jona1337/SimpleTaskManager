package server;


import taskManager.NetFrame;
import taskManager.commands.MessageCommand;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Provider implements Runnable {

    private static final int PORT = 6934;

    private ServerSocket serverSocket;
    private Socket connection;

    private ObjectOutputStream out;
    private ObjectInputStream in;

    private ServerController controller;

    public Provider() {
    }

    public void setController(ServerController controller) {
        this.controller = controller;
    }

    public void run() {
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Waiting for connection");
        try {
            connection = serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Connection received from " + connection.getInetAddress().getHostName());

        try {
            out = new ObjectOutputStream(connection.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            in = new ObjectInputStream(connection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Connection successful with " + connection.getInetAddress().getHostName());
        sendMessage("Connection successful");

        while (true) {
            NetFrame frame = null;
            try {
                frame = (NetFrame) in.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (frame != null) {
                acceptFrame(frame);
                System.out.println("Server got new message!");
            }
        }

    }

    public void sendMessage(String message) {
        sendFrame(new NetFrame(new MessageCommand(message)));
    }

    public void sendFrame(NetFrame frame) {
        try {
            out.writeObject(frame);
            out.flush();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void acceptFrame(NetFrame frame) {

        controller.parseFrame(frame);
    }

}