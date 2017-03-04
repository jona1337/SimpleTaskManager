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

    public Client() {
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    private void connect() {

        System.out.println("Trying to connect...");

        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getByName(ADDRESS);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }

        while (socket == null) {
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

    private void close() {

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

    private void checkConnection() {

        if (socket == null) {
            connect();
            return;

        } else if (!socket.isConnected() || socket.isClosed()) {
            close();
            connect();
        }

    }

    public void run() {

        while (true) {

            checkConnection();

            NetFrame frame = null;
            try {
                frame = (NetFrame) in.readObject();
            } catch (ClassNotFoundException e) {
                System.out.println("Client received unknown frame!");
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }

            if (frame != null) {
                System.out.println("Client got new message!");
                receiveFrame(frame);
            }

        }

    }

    public void sendFrame(NetFrame frame) {

        if (socket == null) {
            System.out.println("Socket is null. Cant send frame");
            return;
        }

        try {
            out.writeObject(frame);
            out.flush();
        } catch (SocketException e) {
            System.out.println("Socket was closed. Cant send frame");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void receiveFrame(NetFrame frame) {
        controller.receiveData(frame.getData());
    }


}
