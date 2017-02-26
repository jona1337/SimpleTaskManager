package taskManager.commands;

import taskManager.CommandTypeEnum;
import taskManager.NetCommand;
import taskManager.Task;

import java.util.Date;

public class EditTaskCommand implements NetCommand {

    private CommandTypeEnum type = CommandTypeEnum.edit;

    private String id;
    private String name;
    private String description;
    private Date date;

    public EditTaskCommand(String id, String name, String description, Date date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
    }

    public CommandTypeEnum getType() {
        return type;
    }

    public String getId() {
        return id;
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
