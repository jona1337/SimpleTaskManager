package client.view.taskEdit;

import client.UserInterfaceController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import logic.Task;
import logic.utils.DateUtils;

import java.util.Date;

public class TaskEditDialogController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField dateField;

    private UserInterfaceController controller;

    private Task task;

    @FXML
    private void initialize() {
    }

    public void setMainController(UserInterfaceController controller) {
        this.controller = controller;
    }

    public void setTask(Task task) {
        this.task = task;

        if (task == null) {
            nameField.clear();
            descriptionField.clear();
            dateField.clear();
        } else {
            nameField.setText(task.getName());
            descriptionField.setText(task.getDescription());
            dateField.setText(DateUtils.format(task.getDate()));
        }
    }


    @FXML
    private void handleOk() {
        if (isInputValid()) {

            Date date = DateUtils.parse(dateField.getText());

            if (isNewTaskMode()) {
                controller.getController().addTaskCommand(nameField.getText(), descriptionField.getText(), date);
                controller.hideEditDialog();
            } else {
                controller.getController().editTaskCommand(task.getID(), nameField.getText(), descriptionField.getText(), date);
                controller.refreshTaskOverviewDetails();
                controller.hideEditDialog();
            }

        }
    }

    @FXML
    private void handleCancel() {
        controller.hideEditDialog();
    }

    private boolean isNewTaskMode() {
        return task == null;
    }

    private boolean isInputValid() {

        boolean isValid = true;
        String errorMessage = "";

        if (nameField.getText() == null || nameField.getText().isEmpty()) {
            errorMessage += "Write name!\n";
            isValid = false;
        }
        if (descriptionField.getText() == null || descriptionField.getText().isEmpty()) {
            errorMessage += "Write description!\n";
            isValid = false;
        }
        if (dateField.getText() == null || dateField.getText().isEmpty()) {
            errorMessage += "Write date!\n";
            isValid = false;
        } else {
            if (!DateUtils.isValidDate(dateField.getText())) {
                errorMessage += "Invalid date!\n";
                isValid = false;
            }
        }

        if (isValid)
            return true;
        else {
            showInvalidFieldsWarning(errorMessage);
            return false;
        }
    }

    private void showInvalidFieldsWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(controller.getPrimaryStage());
        alert.setTitle("Invalid fields");
        alert.setHeaderText("Warning");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInvalidFieldsWarning() {
        showInvalidFieldsWarning("");
    }

}