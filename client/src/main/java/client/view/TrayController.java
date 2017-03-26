package client.view;

import client.UserInterfaceController;
import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class TrayController {

    private static final String iconImageLoc = "MegaphoneIcon.png";

    private UserInterfaceController controller;

    private java.awt.SystemTray tray;
    private java.awt.TrayIcon trayIcon;


    public TrayController() {
    }

    public void setController(UserInterfaceController controller) {
        this.controller = controller;
    }

    public void init() {

        this.tray = java.awt.SystemTray.getSystemTray();

        URL imageUrl = this.getClass().getClassLoader().getResource(iconImageLoc);
        java.awt.Image image = null;
        try {
            image = ImageIO.read(imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.trayIcon = new java.awt.TrayIcon(image);


        Platform.setImplicitExit(false);

        javax.swing.SwingUtilities.invokeLater(this::addAppToTray);

        StackPane layout = new StackPane();
        layout.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.5);"
        );
        layout.setPrefSize(300, 200);

        layout.setOnMouseClicked(event -> controller.getPrimaryStage().hide());

    }

    public void close() {
        tray.remove(trayIcon);
    }

    private void addAppToTray() {

        java.awt.Toolkit.getDefaultToolkit();

        if (!java.awt.SystemTray.isSupported()) {
            System.out.println("No system tray support, application exiting.");
            Platform.exit();
        }

        trayIcon.addActionListener(event -> Platform.runLater(this::showStage));

        java.awt.MenuItem openItem = new java.awt.MenuItem("SimpleTaskManager");
        openItem.addActionListener(event -> Platform.runLater(this::showStage));

        java.awt.Font defaultFont = java.awt.Font.decode(null);
        java.awt.Font boldFont = defaultFont.deriveFont(java.awt.Font.BOLD);
        openItem.setFont(boldFont);

        java.awt.MenuItem exitItem = new java.awt.MenuItem("Exit");
        exitItem.addActionListener(event -> {
            Platform.exit();
        });

        final java.awt.PopupMenu popup = new java.awt.PopupMenu();
        popup.add(openItem);
        popup.addSeparator();
        popup.add(exitItem);
        trayIcon.setPopupMenu(popup);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }

    }

    private void showStage() {
        Stage primaryStage = controller.getPrimaryStage();
        if (primaryStage != null) {
            primaryStage.show();
            primaryStage.toFront();
        }
    }

}
