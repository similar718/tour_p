package cn.xmzt.www.mine.bean;

/**
 * @author tanlei
 * @date 2019/8/12
 * @describe
 */

public class MyCouponBean {

    /**
     * effectDate : 2019-08-01
     * expireDate : 2019-09-13
     * detail : 使用说明1
     * couponId : 1
     * title : 618 大放送 30元全场通用优惠券
     * type : 0
     * priority : 1
     * maxSubtract : 30
     * minConsume : 100
     * fetchDate : 2019-07-03 05:06:55
     */
    private String effectDate;//生效时间
    private String expireDate;//过期时间
    private String detail;//优惠券明细
    private int couponId; //优惠劵ID
    private String title;//标题
    private int type;//类型 0:通用,1:线路,2:酒店,3:门票
    private int priority;//优先级
    private double maxSubtract;//最高减额
    private double minConsume;//最低消费
    private String fetchDate;//领取时间
    private boolean isUsable;//是否可用
    private boolean isDetails;//是否展示详情

    public boolean isDetails() {
        return isDetails;
    }

    public void setDetails(boolean details) {
        isDetails = details;
    }

    public void setEffectDate(String effectDate) {
        this.effectDate = effectDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setMaxSubtract(double maxSubtract) {
        this.maxSubtract = maxSubtract;
    }

    public void setMinConsume(double minConsume) {
        this.minConsume = minConsume;
    }

    public void setFetchDate(String fetchDate) {
        this.fetchDate = fetchDate;
    }

    public String getEffectDate() {
        return effectDate;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public String getDetail() {
        return detail;
    }

    public int getCouponId() {
        return couponId;
    }

    public String getTitle() {
        return title;
    }

    public int getType() {
        return type;
    }

    public int getPriority() {
        return priority;
    }

    public double getMaxSubtract() {
        return maxSubtract;
    }

    public double getMinConsume() {
        return minConsume;
    }

    public String getFetchDate() {
        return fetchDate;
    }

    public boolean isUsable() {
        return isUsable;
    }

    public void setUsable(boolean usable) {
        isUsable = usable;
    }
}
