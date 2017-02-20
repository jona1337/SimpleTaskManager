
package taskManager;

import java.net.*;
import java.io.*;
import java.util.Timer;
import java.util.Date;
import java.util.TimerTask;

public class Manager {

    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 6934;

    public static void main(String[]args)throws Exception {

        InetAddress inetAddress= InetAddress.getByName(ADDRESS);
        Socket socket=new Socket(inetAddress, PORT);

        InputStream inputStream = socket.getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        TaskLog taskLog = (TaskLog)objectInputStream.readObject();
        inputStream.close();
        objectInputStream.close();

        Timer timer = new Timer();

        for (final Task task : taskLog.getTasks()) {
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    System.out.println(task.toString());
                }
            };
            timer.schedule(timerTask, task.getDate());
        }

    }
}
