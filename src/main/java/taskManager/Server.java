package taskManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class Server {

    public static void main(String[]args) throws IOException,ParseException
    {
        Date now=new Date();
        System.out.println(now);
        System.out.println("Input name");
        BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
        String name=reader.readLine();
        System.out.println("Input description");
        String description=reader.readLine();
        System.out.println("Input date");
        String date_string=reader.readLine();
        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Date date=simpleDateFormat.parse(date_string);
        Task task1=new Task(name,description,date);
        /*System.out.println("Result");
        System.out.println(task1.getName());
        System.out.println(task1.getDescription());
        System.out.println(task1.getDate_alert());*/
        List<Task> taskList=new ArrayList<Task>();
        Tasklog tasklog=new Tasklog(taskList);
        tasklog.addTask(task1);
        tasklog.print();

      /*  SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd.MM.yyyy HH:mm");
        String date="20.2.2017 0:34";
        String date2="20.2.2017 0:36";
        Date date1=simpleDateFormat.parse(date);
        Date date3=simpleDateFormat.parse(date2);
       Timer timer=new Timer();
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                System.out.println("Hello");
            }
        };
        TimerTask timerTask1=new TimerTask() {
            @Override
            public void run() {
                System.out.println("Hello");
            }
        };

        timer.schedule(timerTask,date1);
        timer.schedule(timerTask1, date3);
      //  System.out.println(date3.compareTo(date1));
        timer.cancel();
*/
        int port=6934;
        ServerSocket serverSocket=new ServerSocket(port);
        Socket socket=serverSocket.accept();
        System.out.println("Client is connected");
        OutputStream outputStream=socket.getOutputStream();
       ObjectOutputStream objectOutputStream=new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(tasklog);
        objectOutputStream.flush();




    }
}
