package client;


import logic.NetFrame;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client implements Runnable {

    private static final String ADDRESS = "localhost";
    private static final int PORT = 6934;

    private Controller controller;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private boolean isEnable;

    public Client() {
        isEnable = false;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void enable() {

        if (isEnable) return;

        isEnable = true;
        Thread runClientThread = new Thread(this);
        runClientThread.start();
    }

    public void disable() {

        if (!isEnable) return;
        System.out.println("Disable client");
        isEnable = false;
        disconnect();
    }

    private void connect() {

        if (!isEnable) return;

        System.out.println("Trying to connect...");

        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getByName(ADDRESS);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }

        while (socket == null) {

            if (!isEnable) return;

            try {
                socket = new Socket(inetAddress, PORT);
            } catch (IOException e) {
                System.out.println("Connection is failed");
            }
        }
        System.out.println("Successful connection!");

        try {
            out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        try {
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Successful streams initializing!");

        controller.onServerConnectionEstablished();

    }

    private void disconnect() {

        if (!isConnected()) return;

        try {
            in.close();
            out.close();
        } catch (IOException e) {
            System.out.println("Cant close connection streams");
        }

        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("Cant close connection");
        }

        socket = null;
        in = null;
        out = null;

    }

    private boolean isConnected() {
        return (socket != null && in != null && out != null);
    }

    public void run() {

        while (isEnable) {

            if (!isConnected()) {
                connect();
            }

            NetFrame frame = null;
            try {
                frame = (NetFrame) in.readObject();
            } catch (ClassNotFoundException e) {
                System.out.println("Client received unknown frame!");
            } catch (Exception e) {
                disconnect();
                connect();
            }

            if (frame != null) {
                System.out.println("Client got new message!");
                receiveFrame(frame);
            }

        }

    }

    public void sendFrame(NetFrame frame) {

        if (!isConnected()) {
            System.out.println("No connection. Cant send frame");
            return;
        }

        try {
            out.writeObject(frame);
            out.flush();
        } catch (SocketException e) {
            System.out.println("Socket was closed. Cant send frame");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void receiveFrame(NetFrame frame) {
        controller.receiveData(frame.getData());
    }


}
