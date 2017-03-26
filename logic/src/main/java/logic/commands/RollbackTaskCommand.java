package logic.commands;

import logic.NetData;

public class RollbackTaskCommand implements NetData {

    private String id;

    public RollbackTaskCommand(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
