package server;

import logic.NetData;
import logic.NetFrame;
import logic.Task;
import logic.commands.*;

import java.util.ArrayList;


public class Controller {

    private Model model;
    private Server server;

    public Controller() {

        model = new Model();

        server = new Server();
        server.setController(this);

    }

    public void startServer() {

        Thread t = new Thread(server);
        t.start();

    }

    public void receiveData(NetData data) {

        if (data instanceof AddTaskCommand) {
            AddTaskCommand addTaskCommand = (AddTaskCommand) data;
            model.addTask(
                    addTaskCommand.getName(),
                    addTaskCommand.getDescription(),
                    addTaskCommand.getDate());

        } else if (data instanceof DeleteTaskCommand) {
            model.removeTask( ((DeleteTaskCommand) data).getId() );

        } else if (data instanceof EditTaskCommand) {
            EditTaskCommand editTaskCommand = (EditTaskCommand) data;
            model.editTask(
                    editTaskCommand.getId(),
                    editTaskCommand.getName(),
                    editTaskCommand.getDescription(),
                    editTaskCommand.getDate());

        } else if (data instanceof GetTaskListCommand) {

            sendData(new SendTaskListCommand(model.getTasks()));

        }

        //Костыль
        sendData(new SendTaskListCommand(model.getTasks()));

    }

    public void sendData(NetData data) {
        server.sendFrame(new NetFrame(data));
    }

}
