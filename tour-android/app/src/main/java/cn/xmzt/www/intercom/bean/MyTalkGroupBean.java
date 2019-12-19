package cn.xmzt.www.intercom.bean;

/**
 * 我的对讲群
 */
public class MyTalkGroupBean {
    /**
     * 群ID
     */
    private String groupId;
    /**
     * 群标题
     */
    private String title;
    /**
     * 群头像
     */
    private String avatar;
    /**
     * 群成员数
     */
    private int memberNum;
    private boolean isSelect = false;
    public MyTalkGroupBean() {
    }

    public MyTalkGroupBean(String groupId, String title, String avatar) {
        this.groupId = groupId;
        this.title = title;
        this.avatar = avatar;
    }
    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(int memberNum) {
        this.memberNum = memberNum;
    }
}
