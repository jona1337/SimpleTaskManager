package taskManager;

import java.io.Serializable;
import java.util.List;

public class Tasklog implements Serializable {

    private List<Task> tasks;


    public Tasklog(List<Task> tasks)
    {
        this.tasks=tasks;
    }

    public void addTask(Task new_task)
    {
        boolean flg=true;
        for(int i=0;i<tasks.size();i++)
        {
            if(tasks.get(i).getName().equals(new_task.getName()))
            {
                System.out.println("Task with that name already exists");
                flg=false;
            }
        }
        if(flg) {
            tasks.add(new_task);
        }
    }

    public void remove(String name)
    {
        for(int i=0;i<tasks.size();i++)
        {
            if(tasks.get(i).getName().equals(name))
            {
                tasks.remove(i);
                break;
            }
        }
    }
    public int getLength()
    {
        return tasks.size();
    }

    public Task getTask(int i)
    {
        return tasks.get(i);
    }
    public void print()
    {
        for(int i=0;i<tasks.size();i++)
        {
            System.out.println(tasks.get(i).getName());
            System.out.println(tasks.get(i).getDescription());
            System.out.println(tasks.get(i).getDate_alert());
        }
    }





}
