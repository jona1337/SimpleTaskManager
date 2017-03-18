package client.view.taskList;

import client.UserInterfaceController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.Task;
import logic.utils.DateUtils;

import java.util.Date;


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

    @FXML
    private void handleShowTask() {

        Stage stg = new Stage();

        TableColumn<Task, String> name = new TableColumn<>("Name");
        name.setMinWidth(50);
        name.setCellValueFactory(new PropertyValueFactory<Task, String>("name"));
        TableColumn<Task, String> disc = new TableColumn<>("Description");
        disc.setMinWidth(50);
        disc.setCellValueFactory(new PropertyValueFactory<Task, String>("description"));
        TableColumn<Task, Date> d = new TableColumn<>("Date");
        d.setMinWidth(50);
        d.setCellValueFactory(new PropertyValueFactory<Task, Date>("date"));


        ObservableList<Task> tasks = FXCollections.observableArrayList();
        for (int i = 0; i < controller.getController().getModel().getTasks().size(); i++) {
            if (controller.getController().getModel().getTasks().get(i).getStatus()) {
                tasks.add(controller.getController().getModel().getTasks().get(i));
            }
              /*  String n = controller.getController().getModel().getTasks().get(i).getName();
                String disc1 = controller.getController().getModel().getTasks().get(i).getDescription();
                String id = controller.getController().getModel().getTasks().get(i).getID();
                Date date = controller.getController().getModel().getTasks().get(i).getDate();
                Task s = new Task(n, disc1, date);
                tasks.add(s);*/

        }

        TableView tableView = new TableView();
        tableView.setItems(tasks);
        tableView.getColumns().addAll(name, disc, d);
       /* tableView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 1) {
                    String[] ar = tableView.getSelectionModel().getSelectedItems().get(0).toString().split("/");
                    System.out.println(ar[2]);
                }
            }
        });*/
        javafx.scene.control.Button btn = new javafx.scene.control.Button("Close");
        btn.setOnAction(act -> {
            String[] ar = tableView.getSelectionModel().getSelectedItems().get(0).toString().split("/");
            System.out.println("It works" + ar[3]);
            controller.deleteOld(ar[3]);
            stg.close();
        });
        StackPane root = new StackPane();
        //root.getChildren().add(tableView);
        root.getChildren().addAll(tableView, btn);
        Scene scn = new Scene(root, 500, 500);
        stg.setScene(scn);
        stg.setTitle("MyOldList");
        stg.initModality(Modality.WINDOW_MODAL);
        stg.showAndWait();


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