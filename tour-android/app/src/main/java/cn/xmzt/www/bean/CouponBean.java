package cn.xmzt.www.bean;

import java.io.Serializable;

public class CouponBean implements Serializable {
    private int amount;//数量
    private int astrictCondition;//使用上限条件(1:有效期内只能使用一张,2:每天使用限制数量,3:不限量)
    private String beginDate;//开始时间
    private String couponName;//优惠劵名称
    private int couponType;//优惠劵类型(0:通用,1:线路,2:酒店,3:门票)
    private String endDate;//结束时间
    private int id;//
    private double maxSubtract;//最高减额度
    private double minConsume;//最低消费可使用优惠劵
    private String openTime;//开放时间
    private int priority;//优惠劵优先级(123)
    private boolean received;//领取状态 true:已领取 false:未领取*
    private int sendMode;//发放方式(1:新用户注册,2:30天内未登录用户)
    private int useAstrict;//使用上限
    private String detail;


    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAstrictCondition() {
        return astrictCondition;
    }

    public void setAstrictCondition(int astrictCondition) {
        this.astrictCondition = astrictCondition;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public int getCouponType() {
        return couponType;
    }

    public void setCouponType(int couponType) {
        this.couponType = couponType;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMaxSubtract() {
        return maxSubtract;
    }

    public void setMaxSubtract(double maxSubtract) {
        this.maxSubtract = maxSubtract;
    }

    public double getMinConsume() {
        return minConsume;
    }

    public void setMinConsume(double minConsume) {
        this.minConsume = minConsume;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isReceived() {
        return received;
    }

    public void setReceived(boolean received) {
        this.received = received;
    }

    public int getSendMode() {
        return sendMode;
    }

    public void setSendMode(int sendMode) {
        this.sendMode = sendMode;
    }

    public int getUseAstrict() {
        return useAstrict;
    }

    public void setUseAstrict(int useAstrict) {
        this.useAstrict = useAstrict;
    }

    public String getDetail() {
        if(detail==null){
            return "";
        }
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}