
package client;
import javafx.application.Platform;
import logic.Task;
import java.util.*;
import java.util.ArrayList;

public class TimerAction {
    Timer timer;
    Controller controller;
    private static Date now;

    public TimerAction(Controller controller){

        timer=new Timer();
        this.controller=controller;
        this.now=new Date();

    }


public void timerClick(ArrayList<Task> listTask) {
    timer.cancel();
    timer.purge();
    timer = new Timer();
    List<List<Task>> tasks = new ArrayList<>();
    for (int i = 0; i < listTask.size(); i++) {
        boolean flg = true;
        if ((listTask.get(i).getDate().compareTo(now)==1||listTask.get(i).getDate().compareTo(now)==0)
                &&(listTask.get(i).getStatus()==false))
        {
            for (int k = 0; k < tasks.size(); k++) {
                for (int j = 0; j < tasks.get(k).size(); j++) {
                    if (tasks.get(k).get(j).getDate().equals(listTask.get(i).getDate())) {
                        flg = false;
                        tasks.get(k).add(listTask.get(i));
                        break;

                    }
                }
                if (!flg) {
                    break;

                }
            }
        if (flg) {
            List<Task> newlist = new ArrayList<>();
            newlist.add(listTask.get(i));
            tasks.add(newlist);
        }
    }
    }

    for (int i = 0; i < tasks.size(); i++)
    {
        List<Task> newTask = tasks.get(i);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        controller.showTask(newTask);
                    }
                });
            }
        };

        Date date = newTask.get(0).getDate();
        timer.schedule(task, date);
    }
}

    public static Date getNow()
    {
        return  now;
    }

}
