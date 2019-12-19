package cn.xmzt.www.intercom.event;

/**
 * 刷新我的对讲群列表eventbus
 */
public class RefreshMyTalkGroupList {
    public static final int TYPE_REFRESH=1;//刷新界面
    private int type;

    public RefreshMyTalkGroupList(int type) {
        this.type = type;
    }

    public RefreshMyTalkGroupList() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}