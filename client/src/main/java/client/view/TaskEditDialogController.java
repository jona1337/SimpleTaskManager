package client.view;

import client.UserInterfaceController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import logic.Task;
import logic.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TaskEditDialogController {

    private static final String TIME_HOURS_PATTERN = "HH";
    private static final String TIME_MINUTES_PATTERN = "mm";
    private static final SimpleDateFormat TIME_HOURS_FORMAT = new SimpleDateFormat(TIME_HOURS_PATTERN);
    private static final SimpleDateFormat TIME_MINUTES_FORMAT = new SimpleDateFormat(TIME_MINUTES_PATTERN);

    @FXML
    private TextField nameField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField timeHourField;
    @FXML
    private TextField timeMinuteField;

    @FXML
    private CheckBox alarmCheckbox;
    @FXML
    private DatePicker alarmDatePicker;
    @FXML
    private TextField alarmTimeHourField;
    @FXML
    private TextField alarmTimeMinuteField;
    @FXML
    private Label alarmTimeColonLabel;
    @FXML
    private Label alarmDateLabel;
    @FXML
    private Label alarmTimeLabel;


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
            timeHourField.clear();
            timeMinuteField.clear();
            datePicker.setValue(null);
            alarmTimeHourField.clear();
            alarmTimeMinuteField.clear();
            alarmDatePicker.setValue(null);
            alarmCheckbox.setSelected(false);
            disableAlarmArea();

        } else {

            nameField.setText(task.getName());
            descriptionArea.setText(task.getDescription());
            datePicker.setValue(DateUtils.getLocalDate(task.getDate()));
            timeHourField.setText(DateUtils.format(task.getDate(), TIME_HOURS_FORMAT));
            timeMinuteField.setText(DateUtils.format(task.getDate(), TIME_MINUTES_FORMAT));

            if (task.getAlarmDate() != null) {
                enableAlarmArea();
                alarmCheckbox.setSelected(true);
                alarmTimeHourField.setText(DateUtils.format(task.getAlarmDate(), TIME_HOURS_FORMAT));
                alarmTimeMinuteField.setText(DateUtils.format(task.getAlarmDate(), TIME_MINUTES_FORMAT));
                alarmDatePicker.setValue(DateUtils.getLocalDate(task.getAlarmDate()));
            } else {
                alarmTimeHourField.clear();
                alarmTimeMinuteField.clear();
                alarmDatePicker.setValue(null);
                alarmCheckbox.setSelected(false);
                disableAlarmArea();
            }

        }
    }

    private void enableAlarmArea() {
        alarmDatePicker.setDisable(false);
        alarmTimeHourField.setDisable(false);
        alarmTimeMinuteField.setDisable(false);
        alarmTimeColonLabel.setDisable(false);
        alarmDateLabel.setDisable(false);
        alarmTimeLabel.setDisable(false);
    }

    private void disableAlarmArea() {
        alarmDatePicker.setDisable(true);
        alarmTimeHourField.setDisable(true);
        alarmTimeMinuteField.setDisable(true);
        alarmTimeColonLabel.setDisable(true);
        alarmDateLabel.setDisable(true);
        alarmTimeLabel.setDisable(true);
    }

    @FXML
    private void handleCheckAlarm() {
        if (alarmCheckbox.isSelected()) {
            enableAlarmArea();
        } else {
            disableAlarmArea();
        }
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {


            Date date = getDate();
            Date alarmDate = getAlarmDate();

            if (isNewTaskMode()) {
                controller.getController().addTask(nameField.getText(), descriptionArea.getText(), date, alarmDate);
                controller.hideEditDialog();
            } else {
                controller.getController().editTask(task.getID(), nameField.getText(), descriptionArea.getText(), date, alarmDate);
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

        //new
        if (!(timeHourField.getText().isEmpty())) {
            if (!DateUtils.isValidDate(timeHourField.getText(), TIME_HOURS_FORMAT)) {
                errorMessage += "Write correct hours format!\n";
                isValid = false;
            }
        }

        if (!(timeMinuteField.getText().isEmpty())) {
            if (!DateUtils.isValidDate(timeMinuteField.getText(), TIME_MINUTES_FORMAT)) {
                errorMessage += "Write correct minutes format!\n";
                isValid = false;
            }
        }

        if (alarmCheckbox.isSelected()) {

            if (!(alarmTimeHourField.getText().isEmpty())) {
                if (!DateUtils.isValidDate(alarmTimeHourField.getText(), TIME_HOURS_FORMAT)) {
                    errorMessage += "Write correct alarm hours format!\n";
                    isValid = false;
                }
            }

            if (!(alarmTimeMinuteField.getText().isEmpty())) {
                if (!DateUtils.isValidDate(alarmTimeMinuteField.getText(), TIME_MINUTES_FORMAT)) {
                    errorMessage += "Write correct alarm minutes format!\n";
                    isValid = false;
                }
            }

        }

        if (alarmCheckbox.isSelected() && !getAlarmDate().after(new Date())) {
            errorMessage += "Write correct alarm date! Must be after current date.\n";
            isValid = false;
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


    private Date getDate() {

        Date date = new Date();

        if (datePicker.getValue() != null) {
            date = DateUtils.getDate(datePicker.getValue());
            DateUtils.setDateTime(date, new Date());
        }

        //if (!(timeField.getText() == null || timeField.getText().isEmpty())) {

        if (!timeHourField.getText().isEmpty()) {
            Date hoursDate = DateUtils.parse(timeHourField.getText(), TIME_HOURS_FORMAT);
            DateUtils.setDateHours(date, hoursDate);
        }

        if (!timeMinuteField.getText().isEmpty()) {
            Date minutesDate = DateUtils.parse(timeMinuteField.getText(), TIME_MINUTES_FORMAT);
            DateUtils.setDateMinutes(date, minutesDate);
        }

        DateUtils.setDateSecondsToZero(date);

        return date;
    }

    private Date getAlarmDate() {

        Date alarmDate = null;

        if (alarmCheckbox.isSelected()) {

            alarmDate = new Date();

            if (alarmDatePicker.getValue() != null) {
                alarmDate = DateUtils.getDate(alarmDatePicker.getValue());
                DateUtils.setDateTime(alarmDate, new Date());
            }

            //if (!(timeField.getText() == null || timeField.getText().isEmpty())) {

            if (!alarmTimeHourField.getText().isEmpty()) {
                Date hoursDate = DateUtils.parse(alarmTimeHourField.getText(), TIME_HOURS_FORMAT);
                DateUtils.setDateHours(alarmDate, hoursDate);
            }

            if (!alarmTimeMinuteField.getText().isEmpty()) {
                Date minutesDate = DateUtils.parse(alarmTimeMinuteField.getText(), TIME_MINUTES_FORMAT);
                DateUtils.setDateMinutes(alarmDate, minutesDate);
            }

            DateUtils.setDateSecondsToZero(alarmDate);

        }

        return alarmDate;
    }

}