package taskManager;

import java.io.Serializable;

public interface NetCommand extends Serializable {

    CommandTypeEnum getType();

}
