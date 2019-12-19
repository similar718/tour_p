package cn.xmzt.www.selfdrivingtools.event;

public class ShareLocTypeEvent {
    private int type; // 0 关闭分享位置 1 开启分享位置

    public ShareLocTypeEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
