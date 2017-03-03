package logic.commands;

import logic.NetData;

import java.util.Date;

public class EditTaskCommand implements NetData {

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
