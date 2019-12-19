package cn.xmzt.www.mine.bean;

/**
 * @author tanlei
 * @date 2019/8/9
 * @describe 用户积分信息
 */

public class UserScoreBean {

    /**
     * gmtModified : 2019-07-13 11:20:00
     * enableIntegral : 0
     * lockIntegral : 0
     * id : 22
     * gmtCreate : 2019-07-13 11:20:00
     * userId : 186156
     * totalIntegral : 0
     */
    private String gmtModified;
    private int enableIntegral;
    private int lockIntegral;
    private int id;
    private String gmtCreate;
    private int userId;
    private int totalIntegral;

    public void setGmtModified(String gmtModified) {
        this.gmtModified = gmtModified;
    }

    public void setEnableIntegral(int enableIntegral) {
        this.enableIntegral = enableIntegral;
    }

    public void setLockIntegral(int lockIntegral) {
        this.lockIntegral = lockIntegral;
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

    public void setTotalIntegral(int totalIntegral) {
        this.totalIntegral = totalIntegral;
    }

    public String getGmtModified() {
        return gmtModified;
    }

    public int getEnableIntegral() {
        return enableIntegral;
    }

    public int getLockIntegral() {
        return lockIntegral;
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

    public int getTotalIntegral() {
        return totalIntegral;
    }
}
