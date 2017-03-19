package server;

import logic.NetData;
import logic.NetFrame;
import logic.Task;
import logic.commands.*;

import java.util.ArrayList;


public class Controller {

    private Model model;
    private Server server;

    public Controller() {

        model = new Model();

        server = new Server();
        server.setController(this);

    }

    public void startServer() {

        Thread t = new Thread(server);
        t.start();

    }

    public void sendTaskListCommand() {
        sendData(new SendTaskListCommand(model.getTasks()));
    }

    public void receiveData(NetData data) {

        if (data instanceof SendTaskListCommand) {
            model.setTasks(((SendTaskListCommand)data).getTasks());
        } else if (data instanceof GetTaskListCommand) {
            sendTaskListCommand();
        }

    }


    public void sendData(NetData data) {
        server.sendFrame(new NetFrame(data));
    }

}
