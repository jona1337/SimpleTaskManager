package server;

import logic.NetFrame;
import logic.Task;
import logic.commands.SendTaskListCommand;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Connection implements Runnable {

    private Socket connection;
    private Server server;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean running;

    public Connection(Socket connection, Server server) {

        this.running = false;
        this.connection = connection;
        this.server = server;

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

        if (running) return;

        running = true;

        while (running) {

            NetFrame frame = null;
            try {
                frame = (NetFrame) in.readObject();

            } catch (SocketException e) {
                close();
                return;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.exit(1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (frame != null) {
                receiveFrame(frame);
            }

        }

    }

    public void close() {

        running = false;

        try {
            in.close();
            out.close();
        } catch (IOException e) {
            System.out.println("Cant close connection streams");
        }

        try {
            connection.close();
        } catch (IOException e) {
            System.out.println("Cant close connection");
        }

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
        server.getController().parseFrame(frame);
    }



}
