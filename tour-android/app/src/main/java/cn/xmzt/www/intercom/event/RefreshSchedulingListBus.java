package cn.xmzt.www.intercom.event;

/**
 * 刷新行程列表页面eventbus
 */
public class RefreshSchedulingListBus {
    public static final int TYPE_REFRESH=1;//刷新界面
    private int type;

    public RefreshSchedulingListBus(int type) {
        this.type = type;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}