package cn.xmzt.www.intercom.bean;

import android.text.TextUtils;

/**
 * 群成员、司机
 */
public class GroupMemberBean {
    private boolean autoplay;       //是否自动播放(0:否 1：是)
    private String createDate;      //加入时间
    private String describe;        //描述
    private boolean driver;         //是否司机(0：否 1：是)
    private String image;           //头像
    private boolean leader;         //是否领队(0：否 1：是)
    private String licencePlate;    //车牌号
    private String nickname;        //本群昵称
    private boolean official;       //是否官方人员(0:否 1: 是)
    private String orderId;         //订单号
    private boolean payingUser;     //是否参团用户(0:否 1：是)
    private String completePhone;   //完整手机号
    private String phone;           //手机号
    private int role;               //角色(0：普通成员, 1：群主)
    private int tripStatus;// 状态：1待出行 2出行中 3已结束
    private boolean shareLocation;  //是否分享位置(0:否 1：是)
    private int userId = 0;         //成员用户ID
    private String userName;        //用户名
    private boolean isSelect=false;
    private boolean isOnLine=false;//是否在线
    public boolean isAutoplay() {
        return autoplay;
    }

    public void setAutoplay(boolean autoplay) {
        this.autoplay = autoplay;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public void setCreateDate(String createDate) {
         this.createDate = createDate;
     }
     public String getCreateDate() {
         return createDate;
     }

    public void setDriver(boolean driver) {
         this.driver = driver;
     }
     public boolean getDriver() {
         return driver;
     }

    public void setImage(String image) {
         this.image = image;
     }
     public String getImage() {
         return image;
     }

    public void setLeader(boolean leader) {
         this.leader = leader;
     }
     public boolean isLeader() {
         return leader;
     }

    public void setLicencePlate(String licencePlate) {
         this.licencePlate = licencePlate;
     }
     public String getLicencePlate() {
        if(licencePlate==null){
            return "";
        }
         return licencePlate;
     }

    public void setNickname(String nickname) {
         this.nickname = nickname;
     }
     public String getNickname() {
        if(TextUtils.isEmpty(nickname)){
            if (userId == 0){
                nickname = "未设司机";
            } else {
                nickname = userName;
            }
        }
         return nickname;
     }

    public void setOfficial(boolean official) {
         this.official = official;
     }
     public boolean getOfficial() {
         return official;
     }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setPayingUser(boolean payingUser) {
         this.payingUser = payingUser;
     }
     public boolean getPayingUser() {
         return payingUser;
     }

    public String getCompletePhone() {
        return completePhone;
    }
    public void setCompletePhone(String completePhone) {
        this.completePhone = completePhone;
    }

    public void setPhone(String phone) {
         this.phone = phone;
     }
     public String getPhone() {
         return phone;
     }

    public void setRole(int role) {
         this.role = role;
     }
     public int getRole() {
         return role;
     }

    public boolean isShareLocation() {
        return shareLocation;
    }

    public void setShareLocation(boolean shareLocation) {
        this.shareLocation = shareLocation;
    }

    public void setUserId(int userId) {
         this.userId = userId;
     }
     public int getUserId() {
         return userId;
     }

    public void setUserName(String userName) {
         this.userName = userName;
     }
     public String getUserName() {
         if(TextUtils.isEmpty(nickname)){
             userName="";
         }
         return userName;
     }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getOnLineStr() {
        if(isOnLine){
            return "在线";
        }
        return "离线";
    }
    public boolean isOnLine() {
        return isOnLine;
    }
    public void setOnLine(boolean onLine) {
        isOnLine = onLine;
    }

    public int getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(int tripStatus) {
        this.tripStatus = tripStatus;
    }
}