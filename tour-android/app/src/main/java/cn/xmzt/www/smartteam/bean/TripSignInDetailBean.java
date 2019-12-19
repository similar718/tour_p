package cn.xmzt.www.smartteam.bean;

/**
 * 行程签到详请信息
 */
public class TripSignInDetailBean {

    /**
     * id : 675
     * groupId : 2725551778
     * destinationCoordinate : 113.86406629201156,22.58375800548492
     * gatherTimeStamp : 1575618135697
     * nowTimeStamp : 1575618135697
     * signinTimeStamp : 1575618135697
     * isSignin : 0
     */

    private int id; // 集结地id
    private String groupId; // 群组id
    private String destinationCoordinate; // 目的地坐标
    private long nowTimeStamp; // 当前时间戳
    private long gatherTimeStamp; // 集结时间戳
    private long signinTimeStamp; // 签到时间戳
    private int isSignin; // 是否签到 0 否 1 是

    public long getGatherTimeStamp() {
        return gatherTimeStamp;
    }

    public void setGatherTimeStamp(long gatherTimeStamp) {
        this.gatherTimeStamp = gatherTimeStamp;
    }

    public long getSigninTimeStamp() {
        return signinTimeStamp;
    }

    public void setSigninTimeStamp(long signinTimeStamp) {
        this.signinTimeStamp = signinTimeStamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getDestinationCoordinate() {
        return destinationCoordinate;
    }

    public void setDestinationCoordinate(String destinationCoordinate) {
        this.destinationCoordinate = destinationCoordinate;
    }

    public long getNowTimeStamp() {
        return nowTimeStamp;
    }

    public void setNowTimeStamp(long nowTimeStamp) {
        this.nowTimeStamp = nowTimeStamp;
    }

    public int getIsSignin() {
        return isSignin;
    }

    public void setIsSignin(int isSignin) {
        this.isSignin = isSignin;
    }
}
