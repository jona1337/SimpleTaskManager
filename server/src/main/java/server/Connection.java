package server;

import logic.NetFrame;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class Connection implements Runnable {

    private Socket socket;
    private Server server;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean connected;

    public Connection(Socket connection, Server server) {

        this.socket = connection;
        this.server = server;
        this.connected = true;

        try {
            out = new ObjectOutputStream(connection.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        try {
            in = new ObjectInputStream(connection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    public void run() {

        while (connected) {

            NetFrame frame = null;
            try {
                frame = (NetFrame) in.readObject();
            } catch (SocketException e) {
                System.out.println("Connection " + socket.getInetAddress().getHostName() + " failed. Closing connection");
                close();
            } catch (ClassNotFoundException e) {
                System.out.println("Server received unknown frame!");

            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            if (frame != null) {
                receiveFrame(frame);
            }

        }

    }

    public void close() {

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

        connected = false;

        server.onConnectionClosed(this);

    }


    public void sendFrame(NetFrame frame) {

        try {
            out.reset();
        } catch (IOException e) {
            System.out.println("Cant reset out stream!");
            e.printStackTrace();
        }

        try {
            out.writeObject(frame);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveFrame(NetFrame frame) {
        server.getController().receiveData(frame.getData());
    }



}
