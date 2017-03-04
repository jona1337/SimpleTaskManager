package client.view.root;

import client.UserInterfaceController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;

public class RootLayoutController {

    @FXML
    private Label statusLabel;

    private UserInterfaceController controller;

    public void setMainController(UserInterfaceController controller) {
        this.controller = controller;
    }

    public void setStatusLabel(String status) {
        statusLabel.setText(status);
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