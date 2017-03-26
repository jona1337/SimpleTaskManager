package client.view;

import client.UserInterfaceController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import logic.Task;
import logic.TaskState;
import logic.utils.DateUtils;

import java.util.Date;

public class TaskDetailsController {

    private UserInterfaceController controller;
    private Task task;

    @FXML
    private Label nameLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label alarmDateLabel;
    @FXML
    private Label alarmDateInfoLabel;
    @FXML
    private Label stateLabel;

    @FXML
    private Button switchTaskStateButton;

    public void setTask(Task task) {
        this.task = task;

        if (task == null) {
            nameLabel.setText("");
            descriptionLabel.setText("");
            dateLabel.setText("");
            alarmDateLabel.setText("");
            stateLabel.setText("");

        } else {
            nameLabel.setText(task.getName());
            descriptionLabel.setText(task.getDescription());
            dateLabel.setText(DateUtils.format(task.getDate()));

            Date alarmDate = task.getAlarmDate();
            if (alarmDate != null) {
                alarmDateLabel.setText(DateUtils.format(alarmDate));
                alarmDateInfoLabel.setDisable(false);
            } else {
                alarmDateLabel.setText("");
                alarmDateInfoLabel.setDisable(true);
            }

            if (task.getState() == TaskState.COMPLETED) {
                stateLabel.setText("Completed");
                switchTaskStateButton.setText("Rollback");
            } else {
                stateLabel.setText("Incomplete");
                switchTaskStateButton.setText("Complete");
            }
        }

    }

    public void setMainController(UserInterfaceController controller) {
        this.controller = controller;
    }

    @FXML
    private void handleDeleteTask() {
        controller.hideTaskDetailsDialog();
        controller.getController().deleteTask(task.getID());
    }

    @FXML
    private void handleEditTask() {
        //controller.hideTaskDetailsDialog();
        controller.showTaskEditDialog(task);
    }

    @FXML
    private void handleSwitchTaskState() {
        if (task.getState() == TaskState.COMPLETED) {
            controller.getController().rollbackTask(task.getID());
        } else {
            controller.getController().completeTask(task.getID());
        }
        controller.hideTaskDetailsDialog();
    }

    @FXML
    private void handleOk() {
        controller.hideTaskDetailsDialog();
    }

}
