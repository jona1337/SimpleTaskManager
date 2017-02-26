package server;

import taskManager.CommandTypeEnum;
import taskManager.NetCommand;
import taskManager.NetFrame;
import taskManager.commands.AddTaskCommand;
import taskManager.commands.DataCommand;
import taskManager.commands.DeleteTaskCommand;
import taskManager.commands.EditTaskCommand;


public class ServerController {

    private ServerModel serverModel;
    private Provider provider;

    public ServerController() {}

    public ServerModel getServerModel() {
        return serverModel;
    }

    public Provider getProvider() {
        return provider;
    }

    public void start() {
        serverModel = new ServerModel();
        provider = new Provider();
        provider.setController(this);

        Thread t = new Thread(provider);
        t.start();

    }

    public void parseFrame(NetFrame frame) {

        NetCommand command = frame.getCommand();
        CommandTypeEnum type = command.getType();

        if (type == CommandTypeEnum.add) {
            AddTaskCommand addTaskCommand = (AddTaskCommand) command;
            serverModel.addTask(
                    addTaskCommand.getName(),
                    addTaskCommand.getDescription(),
                    addTaskCommand.getDate());
        } else if (type == CommandTypeEnum.del) {
            serverModel.removeTask( ((DeleteTaskCommand)command).getId() );
        } else if (type == CommandTypeEnum.edit) {
            EditTaskCommand editTaskCommand = (EditTaskCommand) command;
            serverModel.editTask(
                    editTaskCommand.getId(),
                    editTaskCommand.getName(),
                    editTaskCommand.getDescription(),
                    editTaskCommand.getDate());
        } else if (type == CommandTypeEnum.get) {

            provider.sendFrame(new NetFrame(new DataCommand(serverModel.getTasks())));

        } else if (type == CommandTypeEnum.close) {
            // idk
        }

        //Костыль
        provider.sendFrame(new NetFrame(new DataCommand(serverModel.getTasks())));

    }

}
