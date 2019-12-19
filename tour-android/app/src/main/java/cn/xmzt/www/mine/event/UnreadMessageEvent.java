package cn.xmzt.www.mine.event;

/**
 * 推送消息
 */
public class UnreadMessageEvent {
    private int unreadCount;

    public UnreadMessageEvent() {
    }

    public UnreadMessageEvent(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }
}
