package cn.xmzt.www.selfdrivingtools.bean;

import cn.xmzt.www.roomdb.beans.GroupUserInfo;

public class SharedNavigationMapRightInfo {
    private String content;
    private int type;
    private boolean isCheck;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public SharedNavigationMapRightInfo(int type) {
        this.type = type;
    }

    public SharedNavigationMapRightInfo() {
    }

    // update info
    String sendId;
    GroupUserInfo bean;
    float distance;

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public GroupUserInfo getBean() {
        return bean;
    }

    public void setBean(GroupUserInfo bean) {
        this.bean = bean;
    }
}
