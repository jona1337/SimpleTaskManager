package taskManager;

import java.io.Serializable;
import  java.util.Date;

public class Task implements Serializable{

    private String name;
    private String description;
    private Date date_alert;

    public Task(String name,String desc,Date date)
    {
        this.name=name;
        this.description=desc;
        this.date_alert=date;
    }

    public String getName()
    {
        return this.name;
    }

    public String getDescription()
    {
        return this.description;
    }

    public Date getDate_alert()
    {
        return this.date_alert;
    }

    public void setName(String name)
    {
        this.name=name;
    }

    public void setDescription(String desc)
    {
        this.description=desc;
    }

    public void setDate_alert(Date date)
    {
        this.date_alert=date;
    }

}
