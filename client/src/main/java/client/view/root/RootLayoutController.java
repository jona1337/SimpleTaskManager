package client.view.root;

import client.MainController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class RootLayoutController {

    private MainController controller;

    public void setMainController(MainController controller) {
        this.controller = controller;
    }

    @FXML
    private void handleShowTasksContent() {
        controller.showTaskOverview();
    }

    @FXML
    private void handleAbout() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.initOwner(controller.getPrimaryStage());
        alert.setTitle("Simple Task Manager");
        alert.setHeaderText("About");
        alert.setContentText("Simple Task Manager\nver: 0.1.0\n");
        alert.showAndWait();
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }
}