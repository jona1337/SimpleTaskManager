package logic.commands;

import logic.NetData;

import java.util.Date;

public class AddTaskCommand implements NetData {

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
