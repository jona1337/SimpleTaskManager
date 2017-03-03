package logic.commands;

import logic.NetData;

public class MessageCommand implements NetData {

    private String message;

    public MessageCommand(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
