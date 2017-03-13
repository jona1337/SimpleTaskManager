package client;


import client.view.root.RootLayoutController;
import client.view.taskEdit.TaskEditDialogController;
import client.view.taskList.TaskListController;
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
import logic.commands.GetTaskListCommand;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;

public class UserInterfaceController extends Application {

    private static final String APP_TITTLE = "Simple Task Manager";
    private static final String EDIT_TASK_DIALOG_TITTLE = "Edit task";
    private static final String NEW_TASK_DIALOG_TITTLE = "New task";
    private static final String iconImageLoc ="MegaphoneIcon.png";


    private Controller controller;

    private Stage primaryStage;
    private BorderPane rootLayout;
    private Parent taskOverview;

    private Stage dialogStage;
    private BorderPane dialogRoot;
    private Parent taskEditDialog;

    private RootLayoutController rootLayoutController;
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

        controller = new Controller();
        controller.setController(this);


        this.primaryStage = primaryStage;
        primaryStage.setTitle(APP_TITTLE);

        initRoot();
        initTaskOverview();

        initDialog();
        initTaskEditDialog();

        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);

        controller.startClient();


        showTaskOverview();
        primaryStage.show();
        //tray
        Platform.setImplicitExit(false);

        javax.swing.SwingUtilities.invokeLater(this::addAppToTray);

        StackPane layout = new StackPane();
        layout.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.5);"
        );
        layout.setPrefSize(300, 200);

        layout.setOnMouseClicked(event -> primaryStage.hide());

        scene.setFill(Color.TRANSPARENT);

        primaryStage.setScene(scene);
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
        rootLayoutController = controller;
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
    //tray
    private void addAppToTray() {
        try {
            java.awt.Toolkit.getDefaultToolkit();

            if (!java.awt.SystemTray.isSupported()) {
                System.out.println("No system tray support, application exiting.");
                Platform.exit();
            }
            java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();

            URL imageUrl = this.getClass().getClassLoader().getResource(iconImageLoc);
            java.awt.Image image = ImageIO.read(imageUrl);
            java.awt.TrayIcon trayIcon = new java.awt.TrayIcon(image);

            trayIcon.addActionListener(event -> Platform.runLater(this::showStage));

            java.awt.MenuItem openItem = new java.awt.MenuItem("SimpleTaskManager");
            openItem.addActionListener(event -> Platform.runLater(this::showStage));

            java.awt.Font defaultFont = java.awt.Font.decode(null);
            java.awt.Font boldFont = defaultFont.deriveFont(java.awt.Font.BOLD);
            openItem.setFont(boldFont);

            java.awt.MenuItem exitItem = new java.awt.MenuItem("Exit");
            exitItem.addActionListener(event -> {
                Platform.exit();
                tray.remove(trayIcon);
            });

            final java.awt.PopupMenu popup = new java.awt.PopupMenu();
            popup.add(openItem);
            popup.addSeparator();
            popup.add(exitItem);
            trayIcon.setPopupMenu(popup);

            tray.add(trayIcon);
        } catch (java.awt.AWTException | IOException e) {
            System.out.println("Unable to init system tray");
            e.printStackTrace();
        }
    }

    private void showStage() {
        if (primaryStage != null) {
            primaryStage.show();
            primaryStage.toFront();
        }
    }

    public void refreshTaskOverviewDetails() {
        taskListController.showTaskDetails();
    }

    public void setAppStatusInfo(String info) {
        rootLayoutController.setStatusLabel(info);
    }

    @Override
    public void stop(){
        controller.stopClient();
    }

    public static void main(String[] args) {
        launch(args);
    }


}
