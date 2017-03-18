package client;


import client.view.root.RootLayoutController;
import client.view.taskEdit.TaskEditDialogController;
import client.view.taskList.TaskListController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import logic.Task;
import logic.commands.GetTaskListCommand;
import logic.commands.SendTaskListCommand;
import logic.utils.DateUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import  java.util.*;

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


    public  void showTask(List<Task> task) {
      // System.out.println(task.getName());
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Task Window");
       // alert.setHeaderText(task.getName());
        //alert.setContentText(task.getDescription());

      /* ButtonType btn1=new ButtonType("Delay");
        ButtonType btn2=new ButtonType("Complete");
        alert.getButtonTypes().setAll(btn1,btn2);
        Optional<ButtonType>res=alert.showAndWait();
        if(res.get()==btn1)
        {
            List<String>choices=new ArrayList<>();
            choices.add("15 min");
            choices.add("45 min");
            choices.add("1 hour");
            choices.add("1 day");
            ChoiceDialog<String>box=new ChoiceDialog<String>("15 min",choices);
            Optional<String>result=box.showAndWait();
            if(box.getSelectedItem().equals("15 min"))
            {
             Calendar calendar=Calendar.getInstance();
                /*calendar.setTime(task.getDate());
                calendar.add(Calendar.MINUTE, 15);
                System.out.println(calendar.getTime());
                task.setDate(calendar.getTime());
                controller.editTaskCommand(task.getID(), task.getName(), task.getDescription(),task.getDate());*/
                /*calendar.setTime(task.get(0).getDate());
                calendar.add(Calendar.MINUTE, 15);
                System.out.println(calendar.getTime());
                controller.editTaskCommand(task.get(0).getID(), task.get(0).getName(), task.get(0).getDescription(),calendar.getTime());
            }
            /*else if(box.getSelectedItem().equals("45 min"))
            {
                Calendar calendar=Calendar.getInstance();
                calendar.setTime(task.getDate());
                calendar.add(Calendar.MINUTE, 45);
                System.out.println(calendar.getTime());
                task.setDate(calendar.getTime());
                controller.editTaskCommand(task.getID(), task.getName(), task.getDescription(),task.getDate());
            }
            else if(box.getSelectedItem().equals("1 hour"))
            {
                Calendar calendar=Calendar.getInstance();
                calendar.setTime(task.getDate());
                calendar.add(Calendar.HOUR, 1);
                System.out.println(calendar.getTime());
                task.setDate(calendar.getTime());
                controller.editTaskCommand(task.getID(), task.getName(), task.getDescription(),task.getDate());
            }
            else if(box.getSelectedItem().equals("1 day"))
            {
                Calendar calendar=Calendar.getInstance();
                calendar.setTime(task.getDate());
                calendar.add(Calendar.DAY_OF_WEEK, 1);
                System.out.println(calendar.getTime());
                task.setDate(calendar.getTime());
                controller.editTaskCommand(task.getID(), task.getName(), task.getDescription(),task.getDate());
            }*/
        //}
        List<String>choices=new ArrayList<>();
        for(int i=0;i<task.size();i++)
        {
            choices.add(task.get(i).getID());
        }
        ChoiceDialog<String>box=new ChoiceDialog<String>(choices.get(0),choices);
        Optional<String>result=box.showAndWait();
        if(result.isPresent())
        {
           /* Calendar calendar=Calendar.getInstance();
                /*calendar.setTime(task.getDate());
                calendar.add(Calendar.MINUTE, 15);
                System.out.println(calendar.getTime());
                task.setDate(calendar.getTime());
                controller.editTaskCommand(task.getID(), task.getName(), task.getDescription(),task.getDate());*/
            /*calendar.setTime(task.get(0).getDate());
            calendar.add(Calendar.MINUTE, 15);
            System.out.println(calendar.getTime());
            controller.editTaskCommand(task.get(0).getID(), task.get(0).getName(), task.get(0).getDescription(),calendar.getTime());*/
            Alert alert1=new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Task Window");
            ButtonType btn1=new ButtonType("Delay");
            ButtonType btn2=new ButtonType("Complete");
            alert1.getButtonTypes().setAll(btn1,btn2);
            Optional<ButtonType>res=alert1.showAndWait();
            Task newTask=null;
            for(int i=0;i<task.size();i++)
            {
                if(task.get(i).getID().equals(result.get()))
                {
                    newTask=task.get(i);
                }
            }
            if(res.get()==btn1) {
                List<String> choices1 = new ArrayList<>();
                choices1.add("15 min");
                choices1.add("45 min");
                choices1.add("1 hour");
                choices1.add("1 day");
                ChoiceDialog<String> box1 = new ChoiceDialog<String>("15 min", choices1);
                Optional<String> result1 = box1.showAndWait();
                if (box1.getSelectedItem().equals("15 min")) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(newTask.getDate());
                    calendar.add(Calendar.MINUTE, 15);
                    controller.editTaskCommand(newTask.getID(),newTask.getName(), newTask.getDescription(), calendar.getTime());
                }
                choices.remove(newTask.getID());
                result=box.showAndWait();

                }
            if(res.get()==btn2)
            {
                controller.completedCommand(newTask.getID());
            }

            }
                /*calendar.setTime(task.get(0).getDate());
                calendar.add(Calendar.MINUTE, 15);
                System.out.println(calendar.getTime());
                controller.editTaskCommand(task.get(0).getID(), task.get(0).getName(), task.get(0).getDescription(),calendar.getTime());

            }
            // alert.setHeaderText(task.getName());
            //alert.setContentText(task.getDescription());

        }

      /*  else if(res.get()==btn2)
        {
           controller.deleteTaskCommand(task.get(0).getID());
        }
*/









      /*  Stage stg=new Stage();

        javafx.scene.control.Button btn=new javafx.scene.control.Button("Close");
        btn.setOnAction(act -> {

            stg.close();
        });

        TableColumn<Task,String> name=new TableColumn<>("Name");
        name.setMinWidth(50);
        name.setCellValueFactory(new PropertyValueFactory<Task, String>("name"));
        TableColumn<Task,String> disc=new TableColumn<>("Description");
        disc.setMinWidth(50);
        disc.setCellValueFactory(new PropertyValueFactory<Task, String>("description"));
        TableColumn<Task,Date> d=new TableColumn<>("Date");
        d.setMinWidth(50);
        d.setCellValueFactory(new PropertyValueFactory<Task, Date>("date"));



        ObservableList<Task> tasks= FXCollections.observableArrayList();
        for(int i=0;i<  controller.getModel().getTasks().size();i++)
        {
                String n = controller.getModel().getTasks().get(i).getName();
                String disc1 = controller.getModel().getTasks().get(i).getDescription();
                String id = controller.getModel().getTasks().get(i).getID();
                Date date = controller.getModel().getTasks().get(i).getDate();
            boolean status=controller.getModel().getTasks().get(i).getStatus();
                Task s = new Task(n, disc1, date);
                s.setStatus(status);
                tasks.add(s);

        }

        TableView <Task>tableView=new TableView<Task>();
        tableView.setItems(tasks);
        tableView.getColumns().addAll(name, disc, d);

        tableView.setRowFactory(new Callback<TableView<Task>, TableRow<Task>>() {
            @Override
            public TableRow call(TableView<Task> paramP) {
                return new TableRow<Task>() {
                    protected void updateItem(Task paramT, boolean paramBoolean) {

                        if (paramT != null) {
                           /* switch (paramT.getName()) {
                                case "fas":
                                    setStyle("-fx-background-color: LIGHTCORAL; -fx-text-background-color: black;");
                                    break;
                                case "vova":
                                    setStyle("-fx-background-color: skyblue; -fx-text-background-color: black;");
                                    break;
                                default:
                                    setStyle(null);
                            }*/
                          /* if(paramT.getStatus())
                            {
                                setStyle("-fx-background-color: LIGHTCORAL; -fx-text-background-color: black;");
                            }
                            else if(!paramT.getStatus())
                            {
                                setStyle("-fx-background-color: green; -fx-text-background-color: black;");
                            }

                        } else {
                            setStyle(null);
                        }
                        super.updateItem(paramT, paramBoolean);


                    }
                };
            }
        });
        tableView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 1) {
                    String[] ar = tableView.getSelectionModel().getSelectedItems().get(0).toString().split("/");
                    System.out.println(ar[2]);
                    Alert alert=new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Task Window");
                   // alert.setHeaderText(task.getName());
                    //alert.setContentText(task.getDescription());

                    ButtonType btn1=new ButtonType("Delay");
                    ButtonType btn2=new ButtonType("Complete");
                    alert.getButtonTypes().setAll(btn1,btn2);
                    Optional<ButtonType>res=alert.showAndWait();
                    if(res.get()==btn1)
                    {
                        List<String>choices=new ArrayList<>();
                        choices.add("15 min");
                        choices.add("45 min");
                        choices.add("1 hour");
                        choices.add("1 day");
                        ChoiceDialog<String>box=new ChoiceDialog<String>("15 min",choices);
                        Optional<String>result=box.showAndWait();
                        if(box.getSelectedItem().equals("15 min"))
                        {
                           /* Calendar calendar=Calendar.getInstance();
                            calendar.setTime(task.getDate());
                            calendar.add(Calendar.MINUTE, 15);
                            System.out.println(calendar.getTime());
                            task.setDate(calendar.getTime());
                            controller.editTaskCommand(task.getID(), task.getName(), task.getDescription(),task.getDate());*/
        //  }
                     /*   else if(box.getSelectedItem().equals("45 min"))
                        {
                           /* Calendar calendar=Calendar.getInstance();
                            calendar.setTime(task.getDate());
                            calendar.add(Calendar.MINUTE, 45);
                            System.out.println(calendar.getTime());
                            task.setDate(calendar.getTime());
                            controller.editTaskCommand(task.getID(), task.getName(), task.getDescription(),task.getDate());*/
        //  }
                     /*   else if(box.getSelectedItem().equals("1 hour"))
                        {
                           /* Calendar calendar=Calendar.getInstance();
                            calendar.setTime(task.getDate());
                            calendar.add(Calendar.HOUR, 1);
                            System.out.println(calendar.getTime());
                            task.setDate(calendar.getTime());
                            controller.editTaskCommand(task.getID(), task.getName(), task.getDescription(),task.getDate());*/
        //   }
                   /*     else if(box.getSelectedItem().equals("1 day"))
                        {
                           /* Calendar calendar=Calendar.getInstance();
                            calendar.setTime(task.getDate());
                            calendar.add(Calendar.DAY_OF_WEEK, 1);
                            System.out.println(calendar.getTime());
                            task.setDate(calendar.getTime());
                            controller.editTaskCommand(task.getID(), task.getName(), task.getDescription(),task.getDate());*/
               /*         }
                    }

                    else if(res.get()==btn2)
                    {
                     //  controller.editTaskCommand(task.getID(),task.getName(),task.getDescription(),task.getDate(),false);
                      //  controller.deleteTaskCommand(task.getID());

                    }
                }
            }
        });
        StackPane root = new StackPane();
        root.getChildren().add(tableView);
        Button btn11=new Button("addd");
        btn11.setOnAction(act -> {
        tableView.getItems().remove(tableView.getSelectionModel().getSelectedIndex());

    });
        root.getChildren().add(btn11);
        Scene scn = new Scene(root, 500, 500);
        stg.setScene(scn);
        stg.setTitle("MyOldList");

        stg.initModality(Modality.WINDOW_MODAL);
        stg.showAndWait();



*/

       /* Stage stg = new Stage();

        javafx.scene.control.Button btn = new javafx.scene.control.Button("Close");
        btn.setOnAction(act -> {

            stg.close();
        });


        TableColumn<Task, String> name = new TableColumn<>("Name");
        name.setMinWidth(50);
        name.setCellValueFactory(new PropertyValueFactory<Task, String>("name"));
        TableColumn<Task, String> disc = new TableColumn<>("Description");
        disc.setMinWidth(50);
        disc.setCellValueFactory(new PropertyValueFactory<Task, String>("description"));
        TableColumn<Task, Date> d = new TableColumn<>("Date");
        d.setMinWidth(50);
        d.setCellValueFactory(new PropertyValueFactory<Task, Date>("date"));


        boolean flg = false;
        Date now = new Date();
        ObservableList<Task> tasks = FXCollections.observableArrayList();
        for (int k = 0; k < task.size(); k++) {
           // if (task.get(k).getDate().compareTo(TimerAction.getNow()) == 1) {

               // {
                    tasks.add(task.get(k));
                   // flg = true;
               // }

            }

           // if (flg) {
                TableView<Task> tableView = new TableView<Task>();
                tableView.setItems(tasks);
                tableView.getColumns().addAll(name, disc, d);
                Button btn11 = new Button("Delete");
                btn11.setOnAction(act -> {
                    String[] ar = tableView.getSelectionModel().getSelectedItems().get(0).toString().split("/");
                    String id = ar[3];
                    controller.deleteTaskCommand(id);
                });
                StackPane root = new StackPane();
                root.getChildren().addAll(tableView, btn11);

                // root.getChildren().add(btn11);
                Scene scn = new Scene(root, 500, 500);
                stg.setScene(scn);
                stg.setTitle("MyOldList");
                stg.initModality(Modality.WINDOW_MODAL);
                stg.showAndWait();

            }
      //  }


*/
    }


    public void deleteOld(String id)
    {
        controller.deleteTaskCommand(id);
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
    public void handleList(javafx.stage.Window mod) throws  Exception{

        Stage stg=new Stage();
        javafx.scene.control.Button btn=new javafx.scene.control.Button("");
        btn.setOnAction(act-> {
            stg.close();
        });
        StackPane root = new StackPane(btn);

        Scene scn = new Scene(root, 150, 100);
        stg.setScene(scn);
        stg.setTitle("Новое модальное окно");
        stg.initModality(Modality.WINDOW_MODAL);
        stg.initOwner(mod);
        stg.showAndWait();

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
