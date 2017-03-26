package logic;

import logic.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Task implements Serializable {

    private String id;

    private String name;
    private String description;
    private Date date;
    private TaskState state;
    private Date alarmDate;

    public Task(String name, String description, Date date) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.id = generateID();
        this.state = TaskState.WAITING;
    }

    public Task(String name, String description, Date date, Date alarmDate) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.alarmDate = alarmDate;
        this.id = generateID();
        this.state = TaskState.WAITING;
    }

    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public TaskState getState() {
        return state;
    }

    public void setState(TaskState state) {
        this.state = state;
    }

    public Date getAlarmDate() {
        return alarmDate;
    }

    public void setAlarmDate(Date alarmDate) {
        this.alarmDate = alarmDate;
    }

    public boolean isExpired() {
        return date.before(new Date());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name)
                .append(" / ")
                .append(description)
                .append(" / ")
                .append(DateUtils.format(date))
                .append(" / ")
                .append(id)
                .append(" / ")
                .append(state.toString());
        return sb.toString();
    }


    private static String generateID() {
        return UUID.randomUUID().toString();
    }



}
