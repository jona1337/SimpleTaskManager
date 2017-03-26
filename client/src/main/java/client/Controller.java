package client;

import javafx.application.Platform;
import logic.NetData;
import logic.NetFrame;
import logic.Task;
import logic.TaskState;
import logic.commands.*;

import java.util.*;

public class Controller {

    private Client client;
    private Model model;
    private AlarmsController alarmsController;
    private UserInterfaceController uiController;

    public Controller() {

        model = new Model();
        client = new Client();
        client.setController(this);
        alarmsController = new AlarmsController();
        alarmsController.setController(this);

    }

    public void setController(UserInterfaceController controller) {
        this.uiController = controller;
    }

    public Model getModel() {
        return model;
    }


    public void init() {
        readFromCache();
        alarmsController.init();
        client.enable();
    }

    public void close() {
        writeToCache();
        client.disable();
        alarmsController.close();
    }


    public void onServerConnectionEstablished() {
        syncData();
    }


    public void sendData(NetData data) {
        client.sendFrame(new NetFrame(data));
    }

    public void receiveData(NetData data) {

        if (data instanceof SendTaskListCommand) {
            ArrayList<Task> tasks = ((SendTaskListCommand) data).getTasks();
            model.addTasks(tasks);
            update();
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



    private void syncData() {
        sendTaskListCommand();
    }

    private void writeToCache() {
        if (!CacheLoader.isCacheExists()) {
            CacheLoader.createCacheFile();
        }
        CacheLoader.writeCache(model.getTasks());
    }

    private void readFromCache() {

        ArrayList<Task> tasks = (ArrayList<Task>) CacheLoader.readCache();
        if (tasks == null) return;
        model.setTasks(tasks);
        update();
    }


    /*-------------------------*/

    public void addTask(String name, String description, Date date, Date alarmDate) {
        model.addTask(name, description, date, alarmDate);
        sendData(new AddTaskCommand(name, description, date, alarmDate));
        update();
    }

    public void deleteTask(String id) {
        model.deleteTask(id);
        sendData(new DeleteTaskCommand(id));
        update();
    }

    public void editTask(String id, String name, String description, Date date, Date alarmDate) {
        model.editTask(id, name, description, date, alarmDate);
        sendData(new EditTaskCommand(id, name, description, date, alarmDate));
        update();
    }

    public void completeTask(String id) {
        model.setTaskState(id, TaskState.COMPLETED);
        sendData(new CompleteTaskCommand(id));
        update();
    }

    public void rollbackTask(String id) {
        model.setTaskState(id, TaskState.WAITING);
        sendData(new RollbackTaskCommand(id));
        update();
    }

    /*-------------------------*/

    private void update() {
        Platform.runLater(() -> {
            uiController.update();
        });
        alarmsController.update();
    }

    /*-------------------------*/

    public void handleTaskAlarm(Task task) {
        task.setAlarmDate(null);
        writeToCache();
        Platform.runLater(() -> {
            uiController.showTaskAlarmDialog(task);;
        });
    }

    public void handleMultiTaskAlarm(ArrayList<Task> tasks) {
        for (Task task : tasks) {
            task.setAlarmDate(null);
        }
        writeToCache();
        Platform.runLater(() -> {
            uiController.showMultiTaskAlarmDialog(tasks);
        });
    }

}
