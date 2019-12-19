package cn.xmzt.www.selfdrivingtools.event;

public class GuideMinTypeEvent {
    private int type; // 1 去这里的界面最小化

    public GuideMinTypeEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
