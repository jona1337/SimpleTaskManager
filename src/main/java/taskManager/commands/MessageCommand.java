package taskManager.commands;

import taskManager.CommandTypeEnum;
import taskManager.NetCommand;

public class MessageCommand extends NetCommand {

    private static final CommandTypeEnum type = CommandTypeEnum.message;

    private String message;

    public MessageCommand(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
