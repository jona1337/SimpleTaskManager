package taskManager.commands;


import taskManager.CommandTypeEnum;
import taskManager.NetCommand;
import taskManager.Task;

import java.util.Date;

public class AddTaskCommand extends NetCommand {

    private static final CommandTypeEnum type = CommandTypeEnum.add;

    private String name;
    private String description;
    private Date date;

    public AddTaskCommand(String name, String description, Date date) {
        this.name = name;
        this.description = description;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }
}
