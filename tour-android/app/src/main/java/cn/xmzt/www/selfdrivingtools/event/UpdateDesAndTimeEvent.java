package cn.xmzt.www.selfdrivingtools.event;

public class UpdateDesAndTimeEvent {
    private int type; // 0 关闭分享位置 1 开启分享位置
    private String groupid;//群编号

    public UpdateDesAndTimeEvent(int type,String groupid) {
        this.type = type;
        this.groupid = groupid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }
}
