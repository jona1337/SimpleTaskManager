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
    private static final int PORT = 6936;

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

        setConnectionStatusInfo("Client is enabled");

        isEnable = true;
        Thread runClientThread = new Thread(this);
        runClientThread.start();
    }

    public void disable() {

        if (!isEnable) return;

        setConnectionStatusInfo("Client is disabled");

        isEnable = false;
        disconnect();
    }

    private void connect() {

        if (!isEnable) return;

        setConnectionStatusInfo("No connection. Trying to connect...");

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
                //
            }
        }

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

        setConnectionStatusInfo("Successful connection! Streams initialized");
        controller.onServerConnectionEstablished();

    }

    private void disconnect() {

        if (!isConnected()) return;

        setConnectionStatusInfo("Disconnecting...");

        try {
            in.close();
            out.close();
        } catch (IOException e) {
            setConnectionStatusInfo("Cant close connection streams");
        }

        try {
            socket.close();
        } catch (IOException e) {
            setConnectionStatusInfo("Cant close connection");
        }

        socket = null;
        in = null;
        out = null;

        setConnectionStatusInfo("Client disconnected");

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
                setConnectionStatusInfo("Client received unknown frame!");
            } catch (Exception e) {
                disconnect();
                connect();
            }

            if (frame != null) {
                receiveFrame(frame);
            }

        }

    }

    public void sendFrame(NetFrame frame) {

        if (!isConnected()) {
            setConnectionStatusInfo("No connection. Cant send frame");
            return;
        }

        try {
            out.writeObject(frame);
            out.flush();
        } catch (SocketException e) {
            setConnectionStatusInfo("Connection was closed. Cant send frame");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }




    private void receiveFrame(NetFrame frame) {
        controller.receiveData(frame.getData());
    }

    private void setConnectionStatusInfo(String info) {
        controller.setAppStatusInfo(info);
    }

}
