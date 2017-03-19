package client;

import javafx.application.Platform;
import logic.NetData;
import logic.NetFrame;
import logic.Task;
import logic.TaskState;
import logic.commands.*;

import java.util.*;

public class Controller implements Observer {

    private Client client;
    private Model model;
    private UserInterfaceController uiController;

    private ArrayList<Timer> taskAlarmTimers;
    private boolean loadUpDataFromServerFlag;

    public Controller() {

        model = new Model();
        model.addObserver(this);
        client = new Client();
        client.setController(this);

        taskAlarmTimers = new ArrayList<>();
        loadUpDataFromServerFlag = true;

    }

    public void setController(UserInterfaceController controller) {
        this.uiController = controller;
    }

    public Model getModel() {
        return model;
    }


    public void init() {
        initData();
        startClient();
    }

    public void close() {
        stopClient();
        writeToCache();
    }


    public void initData() {

        if (CacheLoader.isCacheExists()) {
            loadUpDataFromServerFlag = false;
            readFromCache();
        } else {
            loadUpDataFromServerFlag = true;
        }

    }


    public void startClient() {
        client.enable();
    }

    public void stopClient() {
        client.disable();
    }

    public void onServerConnectionEstablished() {
        if (loadUpDataFromServerFlag) {
            loadUpDataFromServer();
        } else {
            syncData();
        }
    }

    private void loadUpDataFromServer() {
        getTaskListCommand();
    }


    public void sendData(NetData data) {
        client.sendFrame(new NetFrame(data));
    }

    public void receiveData(NetData data) {

        if (data instanceof SendTaskListCommand) {
            if (loadUpDataFromServerFlag) {
                loadUpDataFromServerFlag = false;

                ArrayList<Task> tasks = ((SendTaskListCommand)data).getTasks();
                for (Task task : tasks) {
                    if (task.getState() == TaskState.OCCURRED)
                        task.setState(TaskState.WAITING);
                }
                model.addTasks(tasks);
            }
        }

    }


    public void getTaskListCommand() {
        sendData(new GetTaskListCommand());
    }

    public void sendTaskListCommand() {
        sendData(new SendTaskListCommand(new ArrayList<Task>(model.getTasks())));
    }


    public void setAppStatusInfo(String info) {
        Platform.runLater(() -> {
            uiController.setAppStatusInfo(info);
        });

    }


    private void stopTimers() {
        for(Timer timer : taskAlarmTimers) {
            timer.cancel();
            timer.purge();
        }
        taskAlarmTimers.clear();
    }

    private void refreshTimers() {

        stopTimers();

        List<Task> tasks = new ArrayList<Task>(model.getTasks());

        for(Task task : tasks) {

            if (task.getState() != TaskState.COMPLETED &&
                    task.getState() != TaskState.DEFERRED &&
                    task.getState() != TaskState.OCCURRED) {

                Timer timer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> {
                            task.setState(TaskState.OCCURRED);
                            uiController.showTaskAlarmDialog(task);
                        });
                    }
                };
                timer.schedule(timerTask, task.getDate());
                taskAlarmTimers.add(timer);

            }


        }


    }



    private void syncData() {

        if (loadUpDataFromServerFlag) return;

        sendTaskListCommand();
    }

    private void writeToCache() {

        if (!CacheLoader.isCacheExists()) {
            CacheLoader.createCacheFile();
        }

        CacheLoader.writeCache(new ArrayList<>(model.getTasks()));

    }

    private void readFromCache() {

        ArrayList<Task> tasks = (ArrayList<Task>) CacheLoader.readCache();
        for (Task task : tasks) {
            if (task.getState() == TaskState.OCCURRED)
                task.setState(TaskState.WAITING);
        }
        model.setTasks(tasks);
    }


    /*-------------------------*/

    @Override
    public void update(Observable o, Object arg) {
        refreshTimers();
        writeToCache();
        syncData();
        uiController.refreshTaskOverviewList();
    }
}
