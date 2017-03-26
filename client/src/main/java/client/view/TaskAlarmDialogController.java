package client.view;

import client.UserInterfaceController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import logic.Task;
import logic.utils.DateUtils;

public class TaskAlarmDialogController {

    @FXML
    private Label nameLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label dateLabel;

    private Stage stage;


    private UserInterfaceController controller;
    private Task task;

    public TaskAlarmDialogController() {
    }

    @FXML
    private void initialize() {
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMainController(UserInterfaceController controller) {
        this.controller = controller;
    }

    public void setTask(Task task) {
        this.task = task;
        nameLabel.setText(task.getName());
        descriptionLabel.setText(task.getDescription());
        dateLabel.setText(DateUtils.format(task.getDate()));
    }

    @FXML
    private void handleCompleteTask() {
        stage.close();
        controller.getController().completeTask(task.getID());
    }

    @FXML
    private void handleEditTask() {
        stage.close();
        controller.showTaskEditDialog(task);
    }

    @FXML
    private void handleOk() {
        stage.close();
    }

}
