package cn.xmzt.www.mine.event;

/**
 * @describe
 */
public class RefreshOrderReadEvent {
    public static final int TYPE_READ_REFRESH=1;//已读刷新界面
    private int type;//type表示
    public RefreshOrderReadEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
