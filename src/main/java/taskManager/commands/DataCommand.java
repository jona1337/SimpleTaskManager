package taskManager.commands;

import taskManager.CommandTypeEnum;
import taskManager.NetCommand;
import taskManager.Task;

import java.util.ArrayList;

public class DataCommand extends NetCommand {

    private static final CommandTypeEnum type = CommandTypeEnum.data;

    private ArrayList<Task> tasks;

    public DataCommand(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }
}
