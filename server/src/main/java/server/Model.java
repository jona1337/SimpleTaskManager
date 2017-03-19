package server;


import logic.Task;
import logic.TaskState;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

public class Model implements Serializable {

    private static final String DATA_PACKAGE = "data";
    private static final String TASKS_DATA_FILE = "tasks.data";

    private ArrayList<Task> tasks = new ArrayList<>();

    public Model() {
        initializeData();
    }

    public Model(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /*---------------*/

    private void initializeData() {

        ObjectInputStream in = null;
        try {
            File file = getDataFile();
            if (file.length() > 0) {
                in = new ObjectInputStream(new FileInputStream(file));
                this.tasks = (ArrayList<Task>) in.readObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void saveData() {

        ObjectOutputStream out = null;
        try {

            File file = getDataFile();

            out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(tasks);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private String getAbsoluteDataDir() {
        return new File("").getAbsolutePath() + "\\" + DATA_PACKAGE;
    }

    private String getAbsoluteDataFilePatch() {
        return getAbsoluteDataDir() + "\\" + TASKS_DATA_FILE;
    }

    private File getDataFile() {
        File catalog = new File(getAbsoluteDataDir());
        if (!catalog.exists()) {
            catalog.mkdir();
        }
        File file = new File(getAbsoluteDataFilePatch());
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /*---------------*/

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
        saveData();
    }
}
