package client.view;

import client.UserInterfaceController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import logic.Task;
import logic.TaskState;
import logic.utils.DateUtils;

import java.util.ArrayList;


public class TaskListController {

    @FXML
    private TreeView<String> treeView;

    @FXML
    private TableView<Task> taskTable;
    @FXML
    private TableColumn<Task, String> nameColumn;
    @FXML
    private TableColumn<Task, String> dateColumn;

    @FXML
    private Button switchTaskStateButton;
    @FXML
    private Button editTaskButton;
    @FXML
    private Button deleteTaskButton;


    TreeItem<String> treeTasksItem;
    TreeItem<String> treeIncompleteTasksItem;
    TreeItem<String> treeCompletedTasksItem;



    private UserInterfaceController controller;

    public TaskListController() {
        treeTasksItem = new TreeItem<String> ("Tasks");
        treeTasksItem.setExpanded(true);

        treeIncompleteTasksItem = new TreeItem<String>("Incomplete");
        treeTasksItem.getChildren().add(treeIncompleteTasksItem);

        treeCompletedTasksItem = new TreeItem<String>("Completed");
        treeTasksItem.getChildren().add(treeCompletedTasksItem);

    }

    @FXML
    private void initialize() {

        taskTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

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
                    } else {
                        if (task.isExpired()) {
                            setStyle("-fx-control-inner-background:mistyrose");
                            //setStyle("-fx-control-inner-background:lightyellow");
                        }
                    }
                }
            }
        });


        showTaskDetails(null);

        taskTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showTaskDetails(newValue));

        treeView.setRoot(treeTasksItem);
        treeView.getSelectionModel().select(treeIncompleteTasksItem);

        treeView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> refreshItems());


    }


    public void setMainController(UserInterfaceController controller) {
        this.controller = controller;
    }

    public void refreshItems() {
        ArrayList<Task> tasks = new ArrayList<>();
        if (treeView.getSelectionModel().getSelectedItem() == treeCompletedTasksItem) {
            tasks = controller.getController().getModel().getCompletedTasks();
        } else {
            tasks = controller.getController().getModel().getIncompleteTasks();
        }
        taskTable.setItems(FXCollections.observableArrayList(tasks));
        taskTable.refresh();
    }

    private void showTaskDetails(Task task) {
        if (task == null) {

            editTaskButton.setDisable(true);
            deleteTaskButton.setDisable(true);
            switchTaskStateButton.setVisible(false);

        } else {

            editTaskButton.setDisable(false);
            deleteTaskButton.setDisable(false);
            switchTaskStateButton.setVisible(true);

            if (task.getState() == TaskState.COMPLETED) {
                switchTaskStateButton.setText("Rollback");
            } else {
                switchTaskStateButton.setText("Complete");
            }

        }
    }

    @FXML
    private void handleDeleteTask() {
        for(Task task : taskTable.getSelectionModel().getSelectedItems()) {
            controller.getController().deleteTask(task.getID());
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
    private void handleShowTaskDetails(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Task task = taskTable.getSelectionModel().getSelectedItem();
            if (task != null) {
                controller.showTaskDetails(task);
            }
        }
    }

    @FXML
    private void handleSwitchTaskState() {
        for(Task task : taskTable.getSelectionModel().getSelectedItems()) {
            if (treeView.getSelectionModel().getSelectedItem() == treeCompletedTasksItem) {
                controller.getController().rollbackTask(task.getID());
            } else {
                controller.getController().completeTask(task.getID());
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