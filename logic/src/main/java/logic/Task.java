package logic;

import logic.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Task implements Serializable{

    private String name;
    private String description;
    private Date date;
    private String id;
    private boolean status;

    public Task(String name, String description, Date date) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.id = generateID();
        this.status=false;
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

    public String getID() {
        return id;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name)
                .append(" / ")
                .append(description)
                .append(" / ")
                .append(DateUtils.format(date)).append(" / ").append(id);
        return sb.toString();
    }

    public void setStatus(boolean status)
    {
        this.status=status;
    }
    public boolean getStatus()
    {
        return status;
    }
    public static String generateID() {
        return UUID.randomUUID().toString();
    }

}
