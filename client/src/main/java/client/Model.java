package client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import logic.Task;
import logic.TaskState;

import java.util.*;

public class Model extends Observable {

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
            setChanged();
            notifyObservers();
        });

    }

    public int getTaskCount() {
        return tasks.size();
    }

    /*---------------*/

    public void addTasks(ArrayList<Task> tasks) {
        this.tasks.addAll(tasks);
        System.out.println("addtasks");
        setChanged();
        notifyObservers();
    }

    public Task getTask(String id) {
        for (Task task : tasks) {
            if (task.getID().equals(id))
                return task;
        }
        return null;
    }

    public void addTask(String name, String description, Date date) {
        Task task = new Task(name, description, date);
        tasks.add(task);
        setChanged();
        notifyObservers();
    }

    public void addTask(Task task) {
        tasks.add(task);
        setChanged();
        notifyObservers();
    }

    public void deleteTask(String id) {
        Task task = getTask(id);
        if (task == null) return;
        tasks.remove(task);
        setChanged();
        notifyObservers();
    }

    public void editTask(String id, String name, String description, Date date) {
        Task task = getTask(id);
        if (task == null) return;
        task.setName(name);
        task.setDescription(description);
        task.setDate(date);
        setChanged();
        notifyObservers(task);
    }

    public void completeTask(String id) {
        Task task = getTask(id);
        if (task == null) return;
        task.setState(TaskState.COMPLETED);
        setChanged();
        notifyObservers(task);
    }

    public void deferTask(String id) {
        Task task = getTask(id);
        if (task == null) return;
        task.setState(TaskState.DEFERRED);
        setChanged();
        notifyObservers(task);
    }

    public ArrayList<Task> getSortedTasks() {

        ArrayList<Task> tasks = new ArrayList<>();
        for (Task task : this.tasks) {
            tasks.add(task);
        }

        Collections.sort(tasks, new Comparator<Task>() {
            public int compare(Task o1, Task o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });

        ArrayList<Task> completedTasks = new ArrayList<>();
        ArrayList<Task> waitingTasks = new ArrayList<>();
        ArrayList<Task> deferredTasks = new ArrayList<>();

        for (Task task : this.tasks) {
            if (task.getState() == null ||
                    task.getState() == TaskState.WAITING) {
                waitingTasks.add(task);
            } else if (task.getState() == TaskState.COMPLETED) {
                completedTasks.add(task);
            } else {
                deferredTasks.add(task);
            }
        }

        tasks.clear();
        tasks.addAll(completedTasks);
        tasks.addAll(waitingTasks);
        tasks.addAll(deferredTasks);

        Collections.reverse(tasks);

        return tasks;
    }

}
