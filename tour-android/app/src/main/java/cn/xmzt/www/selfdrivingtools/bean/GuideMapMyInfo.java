package cn.xmzt.www.selfdrivingtools.bean;

public class GuideMapMyInfo {

    /**
     * userId : 186283
     * userName : similar718
     * image : https://pf.xmzt.cn//img/20190905/2532c83f98254432aa395e88c5516481.jpg
     * phone : 134****1206
     * orderId : 1111894499801698304
     * role : 1
     * leader : false
     * driver : false
     * official : false
     * payingUser : true
     * createDate : 2019-09-06 07:36:06
     * shareLocation : false
     * autoplay : true
     */

    private int userId;
    private String userName;
    private String image;
    private String phone;
    private String orderId;
    private int role;
    private int tripStatus;// 状态：1待出行 2出行中 3已结束
    private boolean leader;
    private boolean driver;
    private boolean official;
    private boolean payingUser;
    private String createDate;
    private boolean shareLocation;
    private boolean autoplay;
    private String describe;
    private String licencePlate;
    private String nickname;

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public boolean isLeader() {
        return leader;
    }

    public void setLeader(boolean leader) {
        this.leader = leader;
    }

    public boolean isDriver() {
        return driver;
    }

    public void setDriver(boolean driver) {
        this.driver = driver;
    }

    public boolean isOfficial() {
        return official;
    }

    public void setOfficial(boolean official) {
        this.official = official;
    }

    public boolean isPayingUser() {
        return payingUser;
    }

    public void setPayingUser(boolean payingUser) {
        this.payingUser = payingUser;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
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

    public int getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(int tripStatus) {
        this.tripStatus = tripStatus;
    }
}
