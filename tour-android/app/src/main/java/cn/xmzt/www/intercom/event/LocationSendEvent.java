package cn.xmzt.www.intercom.event;

public class LocationSendEvent {

    /*
    用户Num
     */
    private String groupId;

    public LocationSendEvent(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

}
