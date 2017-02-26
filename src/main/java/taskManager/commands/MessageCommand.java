package taskManager.commands;

import taskManager.CommandTypeEnum;
import taskManager.NetCommand;

public class MessageCommand implements NetCommand {

    private CommandTypeEnum type = CommandTypeEnum.message;

    private String message;

    public MessageCommand(String message) {
        this.message = message;
    }

    public CommandTypeEnum getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
