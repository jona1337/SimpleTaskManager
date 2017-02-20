
package taskManager;

import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.Date;
import java.util.TimerTask;

public class Manager {

    public static void main(String[]args)throws Exception
    {
        int port=6934;
        String address="127.0.0.1";
        InetAddress inetAddress= InetAddress.getByName(address);
        Socket socket=new Socket(inetAddress,port);
        InputStream inputStream= socket.getInputStream();
        ObjectInputStream objectInputStream=new ObjectInputStream(inputStream);
        Tasklog tasklog=(Tasklog)objectInputStream.readObject();
        inputStream.close();
        objectInputStream.close();
        Timer timer=new Timer();
        TimerTask[] timerTasks=new TimerTask[tasklog.getLength()];
        int count=0;
        for(int i=0;i<tasklog.getLength();i++)
        {
            final String name = tasklog.getTask(i).getName();

            timerTasks[i]=new TimerTask() {
                @Override
                public void run() {
                    System.out.println(name);
                }
            };
            Date date=tasklog.getTask(i).getDate_alert();
            timer.schedule(timerTasks[i],date);
        }

        /*SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd.MM.yyyy HH:mm");
        String date="20.2.2017 0:34";
        String date2="20.2.2017 0:36";
        Date date1=simpleDateFormat.parse(date);
        timer.schedule(timerTask,);*/
       // timer.cancel();




    }
}
