package logic.commands;

import logic.NetData;
import logic.Task;

import java.util.ArrayList;

public class SendTaskListCommand implements NetData {

    private ArrayList<Task> tasks;

    public SendTaskListCommand(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }
}
