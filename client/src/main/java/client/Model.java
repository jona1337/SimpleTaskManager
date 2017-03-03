package client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import logic.Task;

import java.util.ArrayList;

public class Model {

    private ObservableList<Task> tasks;

    public Model() {
        tasks = FXCollections.observableArrayList();
    }

    public Model(ObservableList<Task> tasks) {
        this.tasks = tasks;
    }

    public ObservableList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {



        this.tasks.clear();
        this.tasks.addAll(tasks);

    }

}
