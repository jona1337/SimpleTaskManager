package logic.commands;

import logic.NetData;

public class CompletedCommand implements NetData {

    private String id;

    public CompletedCommand(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
