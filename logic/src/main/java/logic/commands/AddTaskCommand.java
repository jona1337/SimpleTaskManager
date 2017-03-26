package logic.commands;

import logic.NetData;

import java.util.Date;

public class AddTaskCommand implements NetData {

    private String name;
    private String description;
    private Date date;
    private Date alarmDate;

    public AddTaskCommand(String name, String description, Date date, Date alarmDate) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.alarmDate = alarmDate;
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

    public Date getAlarmDate() {
        return alarmDate;
    }
}
