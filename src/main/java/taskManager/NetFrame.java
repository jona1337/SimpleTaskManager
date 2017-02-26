package taskManager;

import java.util.ArrayList;

public class NetFrame {

    private Command command;
    private ArrayList<?> arguments;

    public NetFrame(Command command, ArrayList<?> arguments) {
        this.command = command;
        this.arguments = arguments;
    }

    public Command getCommand() {
        return command;
    }

    public ArrayList<?> getArguments() {
        return arguments;
    }

}
