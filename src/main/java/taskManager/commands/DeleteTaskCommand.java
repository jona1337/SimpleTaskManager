package taskManager.commands;

import taskManager.CommandTypeEnum;
import taskManager.NetCommand;

public class DeleteTaskCommand extends NetCommand {

    private static final CommandTypeEnum type = CommandTypeEnum.del;

    private String id;

    public DeleteTaskCommand(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
