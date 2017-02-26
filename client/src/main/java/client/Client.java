package client;


import taskManager.NetFrame;
import taskManager.commands.MessageCommand;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable {

    private static final String ADDRESS = "localhost";
    private static final int PORT = 6934;

    private MainController controller;

    private Socket socket;

    private ObjectOutputStream out;
    private ObjectInputStream in;

    public void setController(MainController controller) {
        this.controller = controller;
    }

    public void run() {
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getByName(ADDRESS);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        System.out.println("Connection...");

        while (socket == null) {
            try {
                socket = new Socket(inetAddress, PORT);
            } catch (IOException e) {

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
                System.out.println("Client got new message!");
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
