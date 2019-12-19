package cn.xmzt.www.intercom.bean;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class GroupRoomBean {
    private String avatar;//群头像
    private String mPhotoUrl;
    private String createDate;//创建时间
    private String dissolveDate;//自动解散时间
    private boolean full; //是否满员(0:否 1: 是)
    private String groupId;//群ID
    private String groupTitle;//群标题
    private String intro;//群介绍
    private String joinPassword;//入群密码
    private int lineId;//行程线路id
    private String lineName;//行程线路名称
    private int tripId;//行程id
    private int groupType;//1智能组队群，2行程群
    private List<GroupLeaderBean> leaderList;//领队列表
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getmPhotoUrl() {
        return mPhotoUrl;
    }

    public void setmPhotoUrl(String mPhotoUrl) {
        this.mPhotoUrl = mPhotoUrl;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public boolean isFull() {
        return full;
    }

    public void setFull(boolean full) {
        this.full = full;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupTitle() {
        if(TextUtils.isEmpty(groupTitle)){
            return lineName;
        }
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public String getIntro() {
        if(intro==null){
            return "";
        }
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getJoinPassword() {
        return joinPassword;
    }

    public void setJoinPassword(String joinPassword) {
        this.joinPassword = joinPassword;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public String getDissolveDate() {
        if(dissolveDate==null){
            return "";
        }
        return dissolveDate;
    }

    public void setDissolveDate(String dissolveDate) {
        this.dissolveDate = dissolveDate;
    }

    public int getLineId() {
        return lineId;
    }

    public void setLineId(int lineId) {
        this.lineId = lineId;
    }

    public String getLineName() {
        if(lineName==null){
            lineName="";
        }
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public int getGroupType() {
        return groupType;
    }

    public void setGroupType(int groupType) {
        this.groupType = groupType;
    }

    public List<GroupLeaderBean> getLeaderList() {
        if(leaderList==null){
            leaderList=new ArrayList<>();
        }
        return leaderList;
    }

    public void setLeaderList(List<GroupLeaderBean> leaderList) {
        this.leaderList = leaderList;
    }
}
