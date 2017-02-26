package client;


import client.view.root.RootLayoutController;
import client.view.taskEdit.TaskEditDialogController;
import client.view.taskList.TaskListController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import taskManager.CommandTypeEnum;
import taskManager.NetCommand;
import taskManager.NetFrame;
import taskManager.Task;
import taskManager.commands.AddTaskCommand;
import taskManager.commands.DataCommand;
import taskManager.commands.DeleteTaskCommand;
import taskManager.commands.EditTaskCommand;

import java.io.IOException;
import java.util.Date;

public class MainController extends Application {

    private static final String APP_TITTLE = "Simple Task Manager";
    private static final String EDIT_TASK_DIALOG_TITTLE = "Edit task";
    private static final String NEW_TASK_DIALOG_TITTLE = "New task";

    private Client client;
    private ClientModel model;

    private Stage primaryStage;
    private BorderPane rootLayout;
    private Parent taskOverview;

    private Stage dialogStage;
    private BorderPane dialogRoot;
    private Parent taskEditDialog;

    private TaskListController taskListController;
    private TaskEditDialogController taskEditDialogController;

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public ClientModel getModel() {
        return model;
    }

    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        primaryStage.setTitle(APP_TITTLE);

        initModel();

        initRoot();
        initTaskOverview();

        initDialog();
        initTaskEditDialog();

        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);

        initClient();

        showTaskOverview();
        primaryStage.show();

    }

    private void initClient() {
        client = new Client();
        client.setController(this);

        Thread t = new Thread(client);
        t.start();

    }

    private void initModel() {
        model = new ClientModel();
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

    public static void main(String[] args) {
        launch(args);
    }


    /*--------------------*/

    public void parseFrame(NetFrame frame) {

        NetCommand command = frame.getCommand();
        CommandTypeEnum type = command.getType();

        System.out.println(type.toString());

        if (type == CommandTypeEnum.data) {
            model.setTasks( ((DataCommand)command).getTasks() );
            taskListController.refreshItems();
        }

    }

    public void addTaskCommand(String name, String description, Date date) {
        client.sendFrame(new NetFrame(new AddTaskCommand(name, description, date)));
    }

    public void editTaskCommand(String id, String name, String description, Date date) {
        client.sendFrame(new NetFrame(new EditTaskCommand(id, name, description, date)));
    }

    public void deleteTaskCommand(String id) {
        client.sendFrame(new NetFrame(new DeleteTaskCommand(id)));
    }



}
