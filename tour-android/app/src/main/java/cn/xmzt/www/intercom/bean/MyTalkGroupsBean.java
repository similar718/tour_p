package cn.xmzt.www.intercom.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的当前对讲群及我的所有对讲群
 */
public class MyTalkGroupsBean {

    private String groupId; // 选中的群id
    private int id; // id
    private int isPosition; // 是否显示成员位置：1 否 2 是
    private int userId; // 用户id
    private List<MyTalkGroupBean> groupUserSetups = new ArrayList(); // 群列表信息

    public List<MyTalkGroupBean> getGroupUserSetups() {
        if(groupUserSetups==null){
            groupUserSetups = new ArrayList(); // 群列表信息
        }
        return groupUserSetups;
    }

    public void setGroupUserSetups(List<MyTalkGroupBean> groupUserSetups) {
        this.groupUserSetups = groupUserSetups;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsPosition() {
        return isPosition;
    }

    public void setIsPosition(int isPosition) {
        this.isPosition = isPosition;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
