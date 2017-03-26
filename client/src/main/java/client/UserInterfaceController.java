package client;


import client.view.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.Task;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class UserInterfaceController extends Application {

    private static final String APP_TITTLE = "Simple Task Manager";
    private static final String EDIT_TASK_DIALOG_TITTLE = "Edit task";
    private static final String NEW_TASK_DIALOG_TITTLE = "New task";
    private static final String ALARM_TASK_DIALOG_TITTLE = "Alarm task!";
    private static final String ALARM_MULTI_TASK_DIALOG_TITTLE = "Multi alarm task!";
    private static final String TASK_DETAILS_TITTLE = "Task info";

    private Controller controller;

    private Stage primaryStage;
    private BorderPane rootLayout;
    private Parent taskOverview;

    private Stage dialogStage;
    private Stage taskDetailsStage;

    private RootLayoutController rootLayoutController;
    private TaskListController taskListController;
    private TaskEditDialogController taskEditDialogController;
    private TaskDetailsController taskDetailsController;

    private TrayController trayController;


    public Controller getController() {
        return this.controller;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        primaryStage.setResizable(false);
        primaryStage.setTitle(APP_TITTLE);

        initRoot();
        initTaskOverview();

        initDialogEdit();

        initTaskDetailsDialog();

        initTray();

        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);

        showTaskOverview();
        primaryStage.show();

        controller = new Controller();
        controller.setController(this);
        controller.init();

    }

    private void initRoot() {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/RootLayout.fxml"));
        BorderPane rootLayout = null;
        try {
            rootLayout = (BorderPane) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.rootLayout = rootLayout;
        RootLayoutController controller = loader.getController();
        rootLayoutController = controller;
        controller.setMainController(this);
    }

    private void initTaskOverview() {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/TaskOverview.fxml"));
        AnchorPane taskOverview = null;
        try {
            taskOverview = (AnchorPane) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.taskOverview = taskOverview;
        TaskListController controller = loader.getController();
        controller.setMainController(this);
        taskListController = controller;

    }

    private void initDialogEdit() {

        Stage dialogStage = new Stage();
        dialogStage.initOwner(primaryStage);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/TaskEditDialog.fxml"));
        AnchorPane taskEditDialog = null;
        try {
            taskEditDialog = (AnchorPane) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BorderPane dialogRoot = new BorderPane();
        dialogRoot.setCenter(taskEditDialog);

        dialogStage.setScene(new Scene(dialogRoot));
        dialogStage.initModality(Modality.WINDOW_MODAL);


        TaskEditDialogController controller = loader.getController();
        controller.setMainController(this);
        taskEditDialogController = controller;


        this.dialogStage = dialogStage;

    }

    private void initTaskDetailsDialog() {

        Stage dialogStage = new Stage();
        dialogStage.initOwner(primaryStage);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/TaskDetails.fxml"));
        AnchorPane taskDetailsDialog = null;
        try {
            taskDetailsDialog = (AnchorPane) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BorderPane dialogRoot = new BorderPane();
        dialogRoot.setCenter(taskDetailsDialog);

        dialogStage.setScene(new Scene(dialogRoot));
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.setTitle(TASK_DETAILS_TITTLE);


        TaskDetailsController controller = loader.getController();
        controller.setMainController(this);
        taskDetailsController = controller;

        this.taskDetailsStage = dialogStage;

    }

    private void initTray() {
        TrayController trayController = new TrayController();
        trayController.setController(this);
        trayController.init();
        this.trayController = trayController;
    }

    public void showTaskOverview() {
        rootLayout.setCenter(taskOverview);
    }

    public void hideEditDialog() {
        dialogStage.close();
    }

    public void hideTaskDetailsDialog() {
        taskDetailsStage.close();
    }

    public void showTaskEditDialog(Task task) {
        taskEditDialogController.setTask(task);
        dialogStage.setTitle(EDIT_TASK_DIALOG_TITTLE);
        Platform.runLater(() -> {
            dialogStage.showAndWait();
        });
    }

    public void showNewTaskDialog() {
        taskEditDialogController.setTask(null);
        dialogStage.setTitle(NEW_TASK_DIALOG_TITTLE);
        dialogStage.showAndWait();
    }

    public void showTaskDetails(Task task) {
        taskDetailsController.setTask(task);
        Platform.runLater(() -> {
            taskDetailsStage.showAndWait();
        });
    }

    public void showTaskAlarmDialog(Task task) {

        Stage alarmDialogStage = new Stage();
        alarmDialogStage.initOwner(primaryStage);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/TaskAlarmDialog.fxml"));
        AnchorPane taskAlarmDialog = null;
        try {
            taskAlarmDialog = (AnchorPane) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BorderPane alarmRoot = new BorderPane();
        alarmRoot.setCenter(taskAlarmDialog);

        alarmDialogStage.setScene(new Scene(alarmRoot));
        alarmDialogStage.setTitle(ALARM_TASK_DIALOG_TITTLE);
        alarmDialogStage.initModality(Modality.WINDOW_MODAL);


        TaskAlarmDialogController taskAlarmDialogController = loader.getController();
        taskAlarmDialogController.setMainController(this);
        taskAlarmDialogController.setStage(alarmDialogStage);

        taskAlarmDialogController.setTask(task);
        alarmDialogStage.showAndWait();
    }

    public void showMultiTaskAlarmDialog(ArrayList<Task> tasks) {

        Stage alarmDialogStage = new Stage();
        alarmDialogStage.initOwner(primaryStage);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/MultiTaskAlarmDialog.fxml"));
        AnchorPane taskAlarmDialog = null;
        try {
            taskAlarmDialog = (AnchorPane) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BorderPane alarmRoot = new BorderPane();
        alarmRoot.setCenter(taskAlarmDialog);

        alarmDialogStage.setScene(new Scene(alarmRoot));
        alarmDialogStage.setTitle(ALARM_MULTI_TASK_DIALOG_TITTLE);
        alarmDialogStage.initModality(Modality.WINDOW_MODAL);


        MultiTaskAlarmDialogController taskAlarmDialogController = loader.getController();
        taskAlarmDialogController.setMainController(this);
        taskAlarmDialogController.setStage(alarmDialogStage);

        taskAlarmDialogController.setTasks(tasks);
        alarmDialogStage.showAndWait();
    }


    public void update() {
        taskListController.refreshItems();
    }

    public void setAppStatusInfo(String info) {
        rootLayoutController.setStatusLabel(info);
    }

    @Override
    public void stop() {
        controller.close();
        trayController.close();
    }

    public static void main(String[] args) {
        launch(args);
    }


}
