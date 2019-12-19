package cn.xmzt.www.mine.bean;

/**
 * @author tanlei
 * @date 2019/8/30
 * @describe
 */

public class WxRegisterBean {
    /**
     * 1已绑定0未绑定"
     */
    private int isTel;
    private String token;

    public int getIsTel() {
        return isTel;
    }

    public void setIsTel(int isTel) {
        this.isTel = isTel;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "WxRegisterBean{" +
                "isTel=" + isTel +
                ", token='" + token + '\'' +
                '}';
    }
}
