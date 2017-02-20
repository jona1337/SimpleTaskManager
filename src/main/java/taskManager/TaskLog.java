package taskManager;

import java.io.Serializable;
import java.util.ArrayList;

public class TaskLog implements Serializable {

    private ArrayList<Task> tasks;

    public TaskLog(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /*---------------*/

    public int getTaskCount() {
        return tasks.size();
    }

    /*---------------*/

    public Task getTask(String id) {
        for (Task task : tasks) {
            if (task.getID().equals(id))
                return task;
        }
        return null;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(String id) {
        Task task = getTask(id);
        if (task == null) return;
        tasks.remove(task);
    }

}
