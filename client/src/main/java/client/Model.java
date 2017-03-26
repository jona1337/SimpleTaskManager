package client;

import javafx.application.Platform;
import logic.Task;
import logic.TaskState;
import logic.utils.DateUtils;

import java.util.*;

public class Model {

    private final ArrayList<Task> tasks;

    public Model() {
        this.tasks = new ArrayList<>();
    }

    public Model(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {

        Platform.runLater(() -> {
            this.tasks.clear();
            this.tasks.addAll(tasks);
        });

    }

    public int getTaskCount() {
        return tasks.size();
    }

    /*---------------*/

    public void addTasks(ArrayList<Task> tasks) {
        for(Task task : tasks) {
            if (getTask(task.getID()) == null)
                this.tasks.add(task);
        }
    }

    public Task getTask(String id) {
        for (Task task : tasks) {
            if (task.getID().equals(id))
                return task;
        }
        return null;
    }

    public void addTask(String name, String description, Date date, Date alarmDate) {
        Task task = new Task(name, description, date, alarmDate);
        tasks.add(task);
    }

    public void addTask(Task task) {
        if (getTask(task.getID()) != null) return;

        this.tasks.add(task);
    }

    public void deleteTask(String id) {
        Task task = getTask(id);
        if (task == null) return;
        tasks.remove(task);
    }

    public void editTask(String id, String name, String description, Date date, Date alarmDate) {
        Task task = getTask(id);
        if (task == null) return;
        task.setName(name);
        task.setDescription(description);
        task.setDate(date);
        task.setAlarmDate(alarmDate);
    }

    public void setTaskState(String id, TaskState state) {
        Task task = getTask(id);
        if (task == null) return;
        task.setState(state);
    }

    public ArrayList<Task> getCompletedTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        for (Task task : this.tasks) {
            if (task.getState() == TaskState.COMPLETED)
                tasks.add(task);
        }
        return tasks;
    }

    public ArrayList<Task> getIncompleteTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        for (Task task : this.tasks) {
            if (task.getState() != TaskState.COMPLETED)
                tasks.add(task);
        }
        return tasks;
    }

    public ArrayList<Task> getTasksByAlarmDate(Date date) {
        ArrayList<Task> tasks = new ArrayList<>();
        for (Task task : this.tasks) {
            if (DateUtils.isFormatEquals(task.getAlarmDate(), date)) {
                tasks.add(task);
            }
        }
        return tasks;
    }

}
