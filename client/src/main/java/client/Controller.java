package client;

import javafx.application.Platform;
import logic.NetData;
import logic.NetFrame;
import logic.Task;
import logic.commands.*;

import java.util.*;

public class Controller {

    Client client;
    Model model;
    UserInterfaceController uiController;
    TimerAction timerAction;
    ArrayList<Task> taskArrayList;

    public Controller() {

        model = new Model();
        client = new Client();
        client.setController(this);
        timerAction = new TimerAction(this);

    }

    public void setController(UserInterfaceController controller) {
        this.uiController = controller;
    }

    public Model getModel() {
        return model;
    }

    public void startClient() {
        client.enable();
    }

    public void stopClient() {
        client.disable();
    }

    public void onServerConnectionEstablished() {
        getTaskListCommand();
    }


    public void showTask(List<Task> task) {
        uiController.showTask(task);
    }


    public void sendData(NetData data) {
        client.sendFrame(new NetFrame(data));
    }

    public void receiveData(NetData data) {
        ArrayList<Task> taskArrayList = null;
        if (data instanceof SendTaskListCommand) {

            model.setTasks(((SendTaskListCommand) data).getTasks());
            timerAction.timerClick(((SendTaskListCommand) data).getTasks());
            System.out.println(TimerAction.getNow());

        }

    }


    public void getTaskListCommand() {
        sendData(new GetTaskListCommand());
    }

    public void addTaskCommand(String name, String description, Date date) {
        sendData(new AddTaskCommand(name, description, date));

    }

    public void editTaskCommand(String id, String name, String description, Date date) {
        sendData(new EditTaskCommand(id, name, description, date));
    }

    public void deleteTaskCommand(String id) {
        sendData(new DeleteTaskCommand(id));

    }

    public void completedCommand(String id) {
        sendData(new CompletedCommand(id));
    }

    public void setAppStatusInfo(String info) {
        Platform.runLater(() -> {
            uiController.setAppStatusInfo(info);
        });

    }


}
