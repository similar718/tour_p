package cn.xmzt.www.bean;

import java.io.Serializable;

public class ToRouteListBus implements Serializable {
    /**
     * 1表示mainActivity选中线路列表
     */
    private int type;

    public ToRouteListBus(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}