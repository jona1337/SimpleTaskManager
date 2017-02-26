package taskManager;

import taskManager.utils.DateUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class Server {

    private static final int PORT = 6934;

    public static void main(String[]args) throws IOException,ParseException {

        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Task("Task1", "LOL", DateUtils.parse("20.02.2016 21.30")));
        tasks.add(new Task("Task2", "LOL2", DateUtils.parse("20.02.2016 21.31")));
        tasks.add(new Task("Task3", "LOL3", DateUtils.parse("20.02.2016 21.32")));

        TaskLog taskLog = new TaskLog(tasks);

        ServerSocket serverSocket = new ServerSocket(PORT);
        Socket socket = serverSocket.accept();
        System.out.println("Client is connected");

        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();

        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(taskLog);
        oos.flush();




    }
}
