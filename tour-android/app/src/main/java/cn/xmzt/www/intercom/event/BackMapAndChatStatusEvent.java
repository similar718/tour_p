package cn.xmzt.www.intercom.event;

/**
 * 位置共享状态变化事件
 * @author Averysk
 */
public class BackMapAndChatStatusEvent {

    private String groupId; // 当前群id
    private int status; // 更新状态 1 finish掉地图界面 2 更改群

    public BackMapAndChatStatusEvent(String groupId, int status) {
        this.groupId = groupId;
        this.status = status;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int isStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
