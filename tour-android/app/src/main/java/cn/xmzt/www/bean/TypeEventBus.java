package cn.xmzt.www.bean;

import java.io.Serializable;

public class TypeEventBus implements Serializable {
    public static final int TYPE_REFRESH=1;//刷新界面
    private int type;

    public TypeEventBus(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}