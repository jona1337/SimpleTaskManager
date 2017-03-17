package client.view.taskList;

import client.UserInterfaceController;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import logic.Task;
import logic.utils.DateUtils;


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

        showTaskDetails(null);

        taskTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showTaskDetails(newValue));

    }



    public void setMainController(UserInterfaceController controller) {
        this.controller = controller;
        taskTable.setItems(controller.getController().getModel().getTasks());
    }
    /*
    public void refreshItems() {
        System.out.println("TaskListController: update data");
        taskTable.setItems(controller.getController().getModel().getTasks());
    }
    */

    private void showTaskDetails(Task task) {
        if (task == null) {

            nameLabel.setText("");
            dateLabel.setText("");
            descriptionLabel.setText("");

        } else {

            nameLabel.setText(task.getName());
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
            controller.getController().deleteTaskCommand(id);
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

    private void showNoSelectedAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(controller.getPrimaryStage());
        alert.setTitle("Incorrect task");
        alert.setHeaderText("Warning");
        alert.setContentText("Please select task");
        alert.showAndWait();
    }


}