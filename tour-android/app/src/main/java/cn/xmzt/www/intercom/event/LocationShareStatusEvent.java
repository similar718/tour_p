package cn.xmzt.www.intercom.event;

/**
 * 位置共享状态变化事件
 * @author Averysk
 */
public class LocationShareStatusEvent {

    private String groupId; // 当前群id
    private String userId;  // 当前用户id
    private boolean status; // 更新状态(true: 开  false: 关)

    public LocationShareStatusEvent(String groupId, String userId, boolean status) {
        this.groupId = groupId;
        this.userId = userId;
        this.status = status;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
