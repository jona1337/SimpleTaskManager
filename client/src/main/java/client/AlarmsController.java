package client;

import javafx.application.Platform;
import logic.Task;
import logic.utils.DateUtils;

import java.util.*;

public class AlarmsController {

    private Controller controller;

    private ArrayList<Timer> taskAlarmTimers;
    private ArrayList<Timer> multiTaskAlarmTimers;

    public AlarmsController() {
        taskAlarmTimers = new ArrayList<>();
        multiTaskAlarmTimers = new ArrayList<>();
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void init() {
        refreshTimers();
    }

    public void close() {
        destroyTimers();
    }


    private void destroyTimers() {
        for (Timer timer : taskAlarmTimers) {
            timer.cancel();
            timer.purge();
        }
        taskAlarmTimers.clear();

        for (Timer timer : multiTaskAlarmTimers) {
            timer.cancel();
            timer.purge();
        }
        multiTaskAlarmTimers.clear();

    }

    public void refreshTimers() {

        destroyTimers();

        HashMap<Date, ArrayList<Task>> hashDateTask = new HashMap<>();

        for (Task task : controller.getModel().getTasks()) {
            Date alarmDate = task.getAlarmDate();
            if (alarmDate != null) {
                if (hashDateTask.get(alarmDate) == null) {
                    hashDateTask.put(alarmDate, new ArrayList<>());
                }
                ArrayList<Task> dateTasks = hashDateTask.get(alarmDate);
                dateTasks.add(task);
            }
        }

        for (Date date : hashDateTask.keySet()) {
            ArrayList<Task> tasks = hashDateTask.get(date);
            if (tasks.size() == 1) {

                Timer timer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        controller.handleTaskAlarm(tasks.get(0));
                    }
                };
                timer.schedule(timerTask, date);
                taskAlarmTimers.add(timer);

            } else if (tasks.size() > 1) {

                Timer timer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        controller.handleMultiTaskAlarm(tasks);
                    }
                };
                timer.schedule(timerTask, date);
                multiTaskAlarmTimers.add(timer);

            }
        }

    }

    public void update() {
        refreshTimers();
    }

}
