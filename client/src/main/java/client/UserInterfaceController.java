package client;


import client.view.root.RootLayoutController;
import client.view.taskEdit.TaskEditDialogController;
import client.view.taskList.TaskListController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.Task;
import logic.commands.GetTaskListCommand;
import logic.commands.SendTaskListCommand;

import java.io.IOException;

public class UserInterfaceController extends Application {

    private static final String APP_TITTLE = "Simple Task Manager";
    private static final String EDIT_TASK_DIALOG_TITTLE = "Edit task";
    private static final String NEW_TASK_DIALOG_TITTLE = "New task";

    private Controller controller;

    private Stage primaryStage;
    private BorderPane rootLayout;
    private Parent taskOverview;

    private Stage dialogStage;
    private BorderPane dialogRoot;
    private Parent taskEditDialog;

    private TaskListController taskListController;
    private TaskEditDialogController taskEditDialogController;

    public Controller getController() {
        return this.controller;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void start(Stage primaryStage) {

        controller = new Controller(this);
        controller.startClient();

        this.primaryStage = primaryStage;
        primaryStage.setTitle(APP_TITTLE);

        initRoot();
        initTaskOverview();

        initDialog();
        initTaskEditDialog();


        controller.sendData(new GetTaskListCommand());

        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);

        showTaskOverview();
        primaryStage.show();

    }

    private void initRoot() {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/root/RootLayout.fxml"));
        BorderPane rootLayout = null;
        try {
            rootLayout = (BorderPane) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.rootLayout = rootLayout;
        RootLayoutController controller = loader.getController();
        controller.setMainController(this);
    }

    private void initTaskOverview() {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/taskList/TaskOverview.fxml"));
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

    private void initDialog() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        BorderPane dialogRoot = new BorderPane();
        Scene scene = new Scene(dialogRoot);
        dialogStage.setScene(scene);
        this.dialogStage = dialogStage;
        this.dialogRoot = dialogRoot;
    }

    private void initTaskEditDialog() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/taskEdit/TaskEditDialog.fxml"));
        AnchorPane taskEditDialog = null;
        try {
            taskEditDialog = (AnchorPane) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.taskEditDialog = taskEditDialog;
        TaskEditDialogController controller = loader.getController();
        controller.setMainController(this);
        taskEditDialogController = controller;

    }

    public void showTaskOverview() {
        refreshTaskOverviewDetails();
        rootLayout.setCenter(taskOverview);
    }


    public void hideEditDialog() {
        dialogStage.close();
    }

    public void showTaskEditDialog(Task task) {
        taskEditDialogController.setTask(task);
        dialogRoot.setCenter(taskEditDialog);
        dialogStage.setTitle(EDIT_TASK_DIALOG_TITTLE);
        dialogStage.showAndWait();
    }

    public void showNewTaskDialog() {
        taskEditDialogController.setTask(null);
        dialogRoot.setCenter(taskEditDialog);
        dialogStage.setTitle(NEW_TASK_DIALOG_TITTLE);
        dialogStage.showAndWait();
    }


    public void refreshTaskOverviewDetails() {
        taskListController.showTaskDetails();
    }

    public void updateData() {
        taskListController.refreshItems();
    }

    public static void main(String[] args) {
        launch(args);
    }


}
