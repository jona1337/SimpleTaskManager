package client.view;

import client.UserInterfaceController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import logic.Task;
import logic.TaskState;
import logic.utils.DateUtils;

import java.util.ArrayList;


public class TaskListController {
    @FXML
    private TableView<Task> taskTable;
    @FXML
    private TableColumn<Task, String> nameColumn;
    @FXML
    private TableColumn<Task, String> dateColumn;

    @FXML
    private Label nameLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label dateLabel;

    private UserInterfaceController controller;

    public TaskListController() {
    }

    @FXML
    private void initialize() {
        nameColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        dateColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(DateUtils.format(cellData.getValue().getDate())));

        taskTable.setRowFactory(tv -> new TableRow<Task>() {
            @Override
            public void updateItem(Task task, boolean empty) {
                super.updateItem(task, empty) ;
                setStyle("");
                if (task != null) {
                    if (task.getState() == TaskState.COMPLETED) {
                        setStyle("-fx-control-inner-background:honeydew");
                    } else if (task.getState() == TaskState.DEFERRED ||
                            task.getState() == TaskState.OCCURRED) {
                        if (task.isExpired()) {
                            setStyle("-fx-control-inner-background:mistyrose");
                        } else {
                            setStyle("-fx-control-inner-background:lightyellow");
                        }
                    }
                }
            }
        });


        showTaskDetails(null);

        taskTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showTaskDetails(newValue));


    }


    public void setMainController(UserInterfaceController controller) {
        this.controller = controller;
    }

    public void refreshItems() {
        ArrayList<Task> tasks = controller.getController().getModel().getSortedTasks();
        taskTable.setItems(FXCollections.observableArrayList(tasks));
        taskTable.refresh();
    }


    private void showTaskDetails(Task task) {
        if (task == null) {

            nameLabel.setText("");
            dateLabel.setText("");
            descriptionLabel.setText("");

        } else {

            nameLabel.setText(task.getName() + " " + task.getState().toString());
            descriptionLabel.setText(task.getDescription());
            dateLabel.setText(DateUtils.format(task.getDate()));

        }
    }

    public void showTaskDetails() {
        showTaskDetails(taskTable.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void handleDeleteTask() {

        Task task = taskTable.getSelectionModel().getSelectedItem();
        if (task != null) {
            String id = task.getID();
            controller.getController().getModel().deleteTask(id);
        } else {
            showNoSelectedAlert();
        }
    }

    @FXML
    private void handleNewTask() {
        controller.showNewTaskDialog();
    }


    @FXML
    private void handleEditTask() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            controller.showTaskEditDialog(selectedTask);
        } else {
            showNoSelectedAlert();
        }
    }

    @FXML
    private void handleShowEditDialog(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Task task = taskTable.getSelectionModel().getSelectedItem();
            if (task != null) {
                controller.showTaskEditDialog(task);
            }
        }
    }

    @FXML
    private void handleCompleteTask() {
        Task task = taskTable.getSelectionModel().getSelectedItem();
        if (task != null) {
            controller.getController().getModel().completeTask(task.getID());
        }
    }

    private void showNoSelectedAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(controller.getPrimaryStage());
        alert.setTitle("Incorrect task");
        alert.setHeaderText("Warning");
        alert.setContentText("Please select task");
        alert.showAndWait();
    }


}