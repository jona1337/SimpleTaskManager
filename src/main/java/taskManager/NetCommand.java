package taskManager;

import java.io.Serializable;

public abstract class NetCommand implements Serializable {

    private static final CommandTypeEnum type = null;

    public static CommandTypeEnum getType() {
        return type;
    }

}
