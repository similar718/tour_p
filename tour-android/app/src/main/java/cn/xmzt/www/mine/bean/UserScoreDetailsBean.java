package cn.xmzt.www.mine.bean;

/**
 * @author tanlei
 * @date 2019/8/9
 * @describe 用户积分明细
 */

public class UserScoreDetailsBean {

    /**
     * gmtCreator : 0
     * enableIntegral : 0
     * lockIntegral : 0
     * outNum : 0
     * id : 0
     * inNum : 0
     * tradeRemake : string
     * gmtCreate : 2019-08-09T03:29:59.687Z
     * userId : 0
     * totalIntegral : 0
     * tradeType : 0
     */
    private int gmtCreator;
    private int enableIntegral;
    private int lockIntegral;
    private int outNum;
    private int id;
    private int inNum;
    private String tradeRemake;
    private String gmtCreate;
    private int userId;
    private int totalIntegral;
    private int tradeType;

    public void setGmtCreator(int gmtCreator) {
        this.gmtCreator = gmtCreator;
    }

    public void setEnableIntegral(int enableIntegral) {
        this.enableIntegral = enableIntegral;
    }

    public void setLockIntegral(int lockIntegral) {
        this.lockIntegral = lockIntegral;
    }

    public void setOutNum(int outNum) {
        this.outNum = outNum;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setInNum(int inNum) {
        this.inNum = inNum;
    }

    public void setTradeRemake(String tradeRemake) {
        this.tradeRemake = tradeRemake;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setTotalIntegral(int totalIntegral) {
        this.totalIntegral = totalIntegral;
    }

    public void setTradeType(int tradeType) {
        this.tradeType = tradeType;
    }

    public int getGmtCreator() {
        return gmtCreator;
    }

    public int getEnableIntegral() {
        return enableIntegral;
    }

    public int getLockIntegral() {
        return lockIntegral;
    }

    public int getOutNum() {
        return outNum;
    }

    public int getId() {
        return id;
    }

    public int getInNum() {
        return inNum;
    }

    public String getTradeRemake() {
        return tradeRemake;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public int getUserId() {
        return userId;
    }

    public int getTotalIntegral() {
        return totalIntegral;
    }

    public int getTradeType() {
        return tradeType;
    }
}
