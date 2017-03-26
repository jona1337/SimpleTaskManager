package logic.commands;

import logic.NetData;

public class CompleteTaskCommand implements NetData {

    private String id;

    public CompleteTaskCommand(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
