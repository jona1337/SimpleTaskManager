package client.view;

import client.UserInterfaceController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import logic.Task;


import java.util.ArrayList;

public class MultiTaskAlarmDialogController {

    private Stage stage;
    private UserInterfaceController controller;

    @FXML
    private ListView<Task> list;

    public MultiTaskAlarmDialogController() {
    }

    @FXML
    private void initialize() {

        list.setCellFactory(param -> new ListCell<Task>() {
            @Override
            protected void updateItem(Task task, boolean empty) {
                super.updateItem(task, empty);
                if (task == null) {
                    setText(null);
                } else {
                    setText(task.getName());
                }
            }
        });

    }

    public void setTasks(ArrayList<Task> tasks) {
        list.setItems(FXCollections.observableArrayList(tasks));
        list.refresh();
    }

    public ObservableList<Task> getTasks() {
        return list.getItems();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMainController(UserInterfaceController controller) {
        this.controller = controller;
    }

    @FXML
    private void handleCompleteAll() {
        stage.close();
        for (Task task : getTasks()) {
            controller.getController().completeTask(task.getID());
        }
    }

    @FXML
    private void handleEditTask() {
        Task task = list.getSelectionModel().getSelectedItem();
        getTasks().remove(task);
        if (getTasks().isEmpty()) {
            stage.close();
        }
        controller.showTaskEditDialog(task);
    }

    @FXML
    private void handleCompleteTask() {
        for (Task task : list.getSelectionModel().getSelectedItems()) {
            controller.getController().completeTask(task.getID());
            getTasks().remove(task);
        }
        if (getTasks().isEmpty()) {
            stage.close();
        }
    }

    @FXML
    private void handleOk() {
        stage.close();
    }


}
