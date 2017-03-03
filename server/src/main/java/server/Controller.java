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

    public void parseFrame(NetFrame frame) {

        NetData data = frame.getData();

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

            ArrayList<Task> tasks = model.getTasks();

            server.sendFrame(new NetFrame(new SendTaskListCommand(tasks)));

        }

        //Костыль
        server.sendFrame(new NetFrame(new SendTaskListCommand(model.getTasks())));

    }

}
