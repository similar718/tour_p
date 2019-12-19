package cn.xmzt.www.mine.event;

/**
 * @author tanlei
 * @date 2019/8/14
 * @describe
 */

public class OrderTypeEvent {
    private int type;

    public OrderTypeEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
