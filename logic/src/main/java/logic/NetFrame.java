package logic;

import java.io.Serializable;

public class NetFrame implements Serializable {

    private NetData data;

    public NetFrame(NetData data) {
        this.data = data;
    }

    public NetData getData() {
        return data;
    }

}
