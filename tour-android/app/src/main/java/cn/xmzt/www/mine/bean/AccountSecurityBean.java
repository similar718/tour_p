package cn.xmzt.www.mine.bean;

/**
 * @author tanlei
 * @date 2019/8/9
 * @describe
 */

public class AccountSecurityBean {

    /**
     * bindTel : true
     * bindSocialSecurity : false
     * bindLoginPassword : true
     * setPayPassword : false
     * bindWeChatAccount : false
     */
    private boolean bindTel;
    private boolean bindSocialSecurity;
    private boolean bindLoginPassword;
    private boolean setPayPassword;
    private boolean bindWeChatAccount;

    public void setBindTel(boolean bindTel) {
        this.bindTel = bindTel;
    }

    public void setBindSocialSecurity(boolean bindSocialSecurity) {
        this.bindSocialSecurity = bindSocialSecurity;
    }

    public void setBindLoginPassword(boolean bindLoginPassword) {
        this.bindLoginPassword = bindLoginPassword;
    }

    public void setSetPayPassword(boolean setPayPassword) {
        this.setPayPassword = setPayPassword;
    }

    public void setBindWeChatAccount(boolean bindWeChatAccount) {
        this.bindWeChatAccount = bindWeChatAccount;
    }

    public boolean isBindTel() {
        return bindTel;
    }

    public boolean isBindSocialSecurity() {
        return bindSocialSecurity;
    }

    public boolean isBindLoginPassword() {
        return bindLoginPassword;
    }

    public boolean isSetPayPassword() {
        return setPayPassword;
    }

    public boolean isBindWeChatAccount() {
        return bindWeChatAccount;
    }
}
