package cn.xmzt.www.selfdrivingtools.event;

public class UpdateGroupDefaultEvent {
    private String groupid;//群编号

    public UpdateGroupDefaultEvent(String groupid) {
        this.groupid = groupid;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }
}
