package cn.xmzt.www.mine.bean;

/**
 * @author tanlei
 * @date 2019/8/15
 * @describe
 */

public class UserWalletBean {

    /**
     * gmtModified : 2019-08-15T07:23:37.246Z
     * lockNum : 0
     * totalNum : 0
     * enableNum : 0
     * id : 0
     * gmtCreate : 2019-08-15T07:23:37.246Z
     * userId : 0
     */
    private String gmtModified;
    private double lockNum;
    private double totalNum;
    private double enableNum;
    private int id;
    private String gmtCreate;
    private int userId;

    public void setGmtModified(String gmtModified) {
        this.gmtModified = gmtModified;
    }

    public void setLockNum(double lockNum) {
        this.lockNum = lockNum;
    }

    public void setTotalNum(double totalNum) {
        this.totalNum = totalNum;
    }

    public void setEnableNum(double enableNum) {
        this.enableNum = enableNum;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getGmtModified() {
        return gmtModified;
    }

    public double getLockNum() {
        return lockNum;
    }

    public double getTotalNum() {
        return totalNum;
    }

    public double getEnableNum() {
        return enableNum;
    }

    public int getId() {
        return id;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public int getUserId() {
        return userId;
    }
}
