package cn.xmzt.www.mine.event;

/**
 * @author tanlei
 * @date 2019/9/17
 * @describe
 */

public class ChangeSecurityEvent {
    private String phoneNumber;

    public ChangeSecurityEvent() {
    }

    public ChangeSecurityEvent(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
