package cn.xmzt.www.smartteam.bean;

import java.util.List;

import cn.xmzt.www.intercom.bean.GroupMemberBean;

/**
 * 智能组队队伍详情
 */
public class SmartTeamInfoBean {
    private String groupId;
    private String groupTitle;//群名称
    private String destination;//集结地点
    private String destinationCoordinate;//集结地点坐标
    private String gatherTime;//集结时间
    private long desId; //查询目的地-集合时间接口返回的id

    private String dissolveDate;//解散时间
    private String  image;//头像
    private String licencePlate;//车牌号
    private String  nickname;//群用户昵称
    private boolean shareLocation;//是否分享位置(0:否 1：是)
    private boolean autoplay;//是否自动播放(0:否 1：是)
    private boolean leader;//是否是领队(0:否 1：是)
    private int role;//角色(0:普通成员 1：群主)

    private String teamPwdcard;//群口令
    private List<GroupMemberBean> groupMemberVOS;//群所有用户信息

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupTitle() {
        if(groupTitle==null){
            return "";
        }
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public String getDestination() {
        if(destination==null){
            return "";
        }
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestinationCoordinate() {
        if(destinationCoordinate==null){
            return "";
        }
        return destinationCoordinate;
    }

    public long getDesId() {
        return desId;
    }

    public void setDesId(long desId) {
        this.desId = desId;
    }

    public void setDestinationCoordinate(String destinationCoordinate) {
        this.destinationCoordinate = destinationCoordinate;
    }

    public String getGatherTime() {
        if(gatherTime==null){
            return "";
        }
        return gatherTime;
    }

    public void setGatherTime(String gatherTime) {
        this.gatherTime = gatherTime;
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

    public String getImage() {
        if(image==null){
            return "";
        }
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLicencePlate() {
        if(licencePlate==null){
            return "";
        }
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    public String getNickname() {
        if(nickname==null){
            return "";
        }
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isShareLocation() {
        return shareLocation;
    }

    public void setShareLocation(boolean shareLocation) {
        this.shareLocation = shareLocation;
    }

    public boolean isAutoplay() {
        return autoplay;
    }

    public void setAutoplay(boolean autoplay) {
        this.autoplay = autoplay;
    }

    public List<GroupMemberBean> getGroupMemberVOS() {
        return groupMemberVOS;
    }

    public void setGroupMemberVOS(List<GroupMemberBean> groupMemberVOS) {
        this.groupMemberVOS = groupMemberVOS;
    }

    public boolean isLeader() {
        return leader;
    }

    public void setLeader(boolean leader) {
        this.leader = leader;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getTeamPwdcard() {
        if(teamPwdcard==null){
            return "";
        }
        return teamPwdcard;
    }

    public void setTeamPwdcard(String teamPwdcard) {
        this.teamPwdcard = teamPwdcard;
    }
}
