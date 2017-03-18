package client.view.taskEdit;

import client.UserInterfaceController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import logic.Task;
import logic.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TaskEditDialogController {

    private static final String TIME_PATTERN = "HH:mm";
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(TIME_PATTERN);

    @FXML
    private TextField nameField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField timeField;


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
            descriptionArea.clear();
            timeField.clear();
            datePicker.setValue(null);

        } else {
            nameField.setText(task.getName());
            descriptionArea.setText(task.getDescription());
            datePicker.setValue(DateUtils.getLocalDate(task.getDate()));
            timeField.setText(DateUtils.format(task.getDate(), TIME_FORMAT));
        }
    }


    @FXML
    private void handleOk() {
        if (isInputValid()) {

            Calendar calendar = Calendar.getInstance();

            Calendar timeCalendar = Calendar.getInstance();
            Date timeDate = new Date();

            if (datePicker.getValue() != null) {
                Date date = DateUtils.getDate(datePicker.getValue());
                calendar.setTime(date);
            }

            if (!(timeField.getText() == null || timeField.getText().isEmpty())) {

                timeDate = DateUtils.parse(timeField.getText(), TIME_FORMAT);
                timeCalendar = Calendar.getInstance();

            }


            timeCalendar.setTime(timeDate);
            calendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));


            Date date = calendar.getTime();

            if (isNewTaskMode()) {
                controller.getController().addTaskCommand(nameField.getText(), descriptionArea.getText(), date);
                controller.hideEditDialog();
            } else {
                controller.getController().editTaskCommand(task.getID(), nameField.getText(), descriptionArea.getText(), date);
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
        if (descriptionArea.getText() == null || descriptionArea.getText().isEmpty()) {
            errorMessage += "Write description!\n";
            isValid = false;
        }
        //new
        if (!((timeField.getText() == null)|| timeField.getText().isEmpty())) {
            if (!DateUtils.isValidDate(timeField.getText(), TIME_FORMAT)) {
                errorMessage += "Write correct format time! 12:00\n";
                isValid = false;
            }
        }
       /* if (dateField.getValue() == null) {
            errorMessage += "Write date!\n";
            isValid = false;
        } else {
            if (!DateUtils.isValidDate(dateField.getValue().toString())) {
                errorMessage += "Invalid date!\n";
                isValid = false;
            }
        }*/

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