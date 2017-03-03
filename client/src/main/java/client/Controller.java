package client;

import logic.NetData;
import logic.NetFrame;
import logic.commands.AddTaskCommand;
import logic.commands.DeleteTaskCommand;
import logic.commands.EditTaskCommand;
import logic.commands.SendTaskListCommand;

import java.util.Date;

public class Controller {

    Client client;
    Model model;
    UserInterfaceController uiController;

    public Controller(UserInterfaceController uiController) {

        model = new Model();
        client = new Client();
        client.setController(this);
        this.uiController = uiController;
        //System.out.println("Set controller 1");
        //uiController.setController(this);
        //System.out.println("Set controller 2");

    }

    public Model getModel() {
        return model;
    }

    public void startClient() {
        Thread clientThread = new Thread(client);
        clientThread.start();
    }


    public void sendData(NetData data) {
        client.sendFrame(new NetFrame(data));
    }

    public void receiveData(NetData data) {
        if (data instanceof SendTaskListCommand) {
            model.setTasks( ((SendTaskListCommand)data).getTasks() );

            // --------- КОСТЫЛЬ!111
            uiController.updateData();
            // ---------
        }
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



}
