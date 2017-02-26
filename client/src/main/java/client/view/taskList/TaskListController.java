package client.view.taskList;

import client.MainController;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import taskManager.Task;
import taskManager.utils.DateUtils;

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

    private MainController controller;

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



    public void setMainController(MainController controller) {
        this.controller = controller;
        taskTable.setItems(controller.getModel().getTasks());
    }

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
            controller.deleteTaskCommand(id);
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

    private void showNoSelectedAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(controller.getPrimaryStage());
        alert.setTitle("Incorrect task");
        alert.setHeaderText("Warning");
        alert.setContentText("Please select task");
        alert.showAndWait();
    }


}