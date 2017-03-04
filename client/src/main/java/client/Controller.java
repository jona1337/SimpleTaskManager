package client;

import javafx.application.Platform;
import logic.NetData;
import logic.NetFrame;
import logic.Task;
import logic.commands.*;

import java.util.ArrayList;
import java.util.Date;

public class Controller {

    Client client;
    Model model;
    UserInterfaceController uiController;

    public Controller() {

        model = new Model();
        client = new Client();
        client.setController(this);
        //System.out.println("Set controller 1");
        //uiController.setController(this);
        //System.out.println("Set controller 2");

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

    public void sendData(NetData data) {
        client.sendFrame(new NetFrame(data));
    }

    public void receiveData(NetData data) {

        if (data instanceof SendTaskListCommand) {

            model.setTasks( ((SendTaskListCommand)data).getTasks() );

            // --------- КОСТЫЛЬ!111
            //uiController.updateData();
            // ---------
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


    public void setAppStatusInfo(String info) {
        Platform.runLater(() -> {
            uiController.setAppStatusInfo(info);
        });

    }


}
