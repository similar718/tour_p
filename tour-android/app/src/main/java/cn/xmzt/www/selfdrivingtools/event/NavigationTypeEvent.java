package cn.xmzt.www.selfdrivingtools.event;

public class NavigationTypeEvent {
    private int type; // 0 finish guide 1 update navigation right view info 2 关闭导航声音 3 开启导航声音

    public NavigationTypeEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
