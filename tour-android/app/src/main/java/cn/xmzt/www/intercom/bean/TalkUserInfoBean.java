package cn.xmzt.www.intercom.bean;

/**
 * 云信用户信息
 * @author Averysk
 */
public class TalkUserInfoBean {
    /**
     * 云信id
     */
    private String accid;
    /**
     * 云信token
     */
    private String token;

    public String getAccid() {
        return accid;
    }

    public void setAccid(String accid) {
        this.accid = accid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
