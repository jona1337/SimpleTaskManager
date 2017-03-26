package server;

import logic.NetData;
import logic.NetFrame;
import logic.Task;
import logic.TaskState;
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

    /*---------*/

    public void receiveData(NetData data) {

        if (data instanceof SendTaskListCommand) {
            sendTaskListCommand();
            model.addTasks(((SendTaskListCommand) data).getTasks());
        } else if (data instanceof GetTaskListCommand) {
            sendTaskListCommand();
        } else if (data instanceof AddTaskCommand) {
            AddTaskCommand addTaskCommand = (AddTaskCommand) data;
            model.addTask(
                    addTaskCommand.getName(),
                    addTaskCommand.getDescription(),
                    addTaskCommand.getDate(),
                    addTaskCommand.getAlarmDate());

        } else if (data instanceof DeleteTaskCommand) {
            model.removeTask(((DeleteTaskCommand) data).getId());

        } else if (data instanceof EditTaskCommand) {
            EditTaskCommand editTaskCommand = (EditTaskCommand) data;
            model.editTask(
                    editTaskCommand.getId(),
                    editTaskCommand.getName(),
                    editTaskCommand.getDescription(),
                    editTaskCommand.getDate(),
                    editTaskCommand.getAlarmDate());

        } else if (data instanceof CompleteTaskCommand) {
            CompleteTaskCommand completeTaskCommand = (CompleteTaskCommand) data;
            model.setTaskState(completeTaskCommand.getId(), TaskState.COMPLETED);

        } else if (data instanceof RollbackTaskCommand) {
            RollbackTaskCommand rollbackTaskCommand = (RollbackTaskCommand) data;
            model.setTaskState(rollbackTaskCommand.getId(), TaskState.WAITING);
        }

    }

    public void sendData(NetData data) {
        server.sendFrame(new NetFrame(data));
    }

    /*---------*/

    public void sendTaskListCommand() {
        sendData(new SendTaskListCommand(model.getTasks()));
    }

}
