package cn.xmzt.www.home.event;

public class CustomizeRefreshBus {
    public static final int TYPE_REFRESH=1;//刷新界面
    public static final int TYPE_DEL=2;//删除刷新界面
    private int type;

    public CustomizeRefreshBus(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}