package taskManager.commands;

import taskManager.CommandTypeEnum;
import taskManager.NetCommand;

public class DeleteTaskCommand implements NetCommand {

    private CommandTypeEnum type = CommandTypeEnum.del;

    private String id;

    public DeleteTaskCommand(String id) {
        this.id = id;
    }

    public CommandTypeEnum getType() {
        return type;
    }

    public String getId() {
        return id;
    }
}
