package client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import taskManager.Task;

import java.util.ArrayList;
import java.util.Date;

public class ClientModel {

    private ObservableList<Task> tasks = FXCollections.observableArrayList();

    public ClientModel() {
    }

    public ClientModel(ObservableList<Task> tasks) {
        this.tasks = tasks;
    }

    public ObservableList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = FXCollections.observableArrayList(tasks);
    }

}
