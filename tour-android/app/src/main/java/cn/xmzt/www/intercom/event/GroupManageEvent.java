package cn.xmzt.www.intercom.event;

import cn.xmzt.www.intercom.bean.GroupMemberBean;

/**
 * 群管理eventbus
 */
public class GroupManageEvent {

    private int type;//1 添加司机 2移除司机 3转让车辆或者更换司机 11.添加成员 12.成员被移除 22修改群介绍 23修改群名称 24延长解散日期
    private GroupMemberBean mGroupMemberBean;

    public GroupManageEvent(int type) {
        this.type = type;
    }

    public GroupManageEvent(int type, GroupMemberBean mGroupMemberBean) {
        this.type = type;
        this.mGroupMemberBean = mGroupMemberBean;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public GroupMemberBean getmGroupMemberBean() {
        return mGroupMemberBean;
    }

    public void setmGroupMemberBean(GroupMemberBean mGroupMemberBean) {
        this.mGroupMemberBean = mGroupMemberBean;
    }
}