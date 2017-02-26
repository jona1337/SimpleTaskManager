package taskManager;

import java.io.Serializable;
import java.util.ArrayList;

public class NetFrame implements Serializable {

    private NetCommand command;

    public NetFrame(NetCommand command) {
        this.command = command;
    }

    public NetCommand getCommand() {
        return command;
    }

    public void setCommand(NetCommand command) {
        this.command = command;
    }
}
