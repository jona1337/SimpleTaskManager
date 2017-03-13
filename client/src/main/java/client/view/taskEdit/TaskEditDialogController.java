package client.view.taskEdit;

import client.UserInterfaceController;
import com.gluonhq.charm.glisten.control.TimePicker;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import logic.Task;
import logic.utils.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TaskEditDialogController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField descriptionField;
    @FXML
    private DatePicker dateField;
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
            descriptionField.clear();
            timeField.clear();
            dateField.setValue(null);

        } else {
            nameField.setText(task.getName());
            descriptionField.setText(task.getDescription());
            DateFormat timeFormat = new SimpleDateFormat("HH:mm");
            timeField.setText(timeFormat.format(task.getDate()));
            dateField.setValue(Instant.ofEpochMilli(task.getDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
        }
    }


    @FXML
    private void handleOk() throws ParseException {
        if (isInputValid()) {

            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
            DateFormat timeFormat = new SimpleDateFormat("HH:mm");
            Date dateFormatObj = new Date();
            DateFormat dateTimeFormat = new SimpleDateFormat("dd.MM.yy HH:mm");
            DateFormat datePickerFormat = new SimpleDateFormat("yy-MM-dd HH:mm");

            Date date;
            if (dateField.getValue() == null) {
                date = dateTimeFormat.parse(dateFormat.format(dateFormatObj)+" "+timeField.getText());
            }
            else if ((timeField.getText() == null)||(timeField.getText().isEmpty())){
                date = datePickerFormat.parse(dateField.getValue().toString()+" "+timeFormat.format(dateFormatObj));

            }
            else {
                date = datePickerFormat.parse(dateField.getValue().toString()+" "+timeField.getText());
            }

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
        //new
        if (!((timeField.getText() == null)|| timeField.getText().isEmpty())) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            if (DateUtils.isValidDate(timeField.getText(), timeFormat)) {
            } else {
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