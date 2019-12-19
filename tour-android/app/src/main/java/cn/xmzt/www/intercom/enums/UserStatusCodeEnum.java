package cn.xmzt.www.intercom.enums;

public enum UserStatusCodeEnum {

    STATUS_KICK_OUT(-1, "该用户被踢出,请重新登录"),
    STATUS_LOGINED(0, "登录成功"),
    STATUS_UNLOGIN(1, "未登录"),
    STATUS_LOGINING(2, "登录中..."),
    STATUS_CONNECTING(3, "连接中..."),
    STATUS_NET_BROKEN(4, "当前网络不可用"),
;


    private int code;
    private String value;

    UserStatusCodeEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code + "";
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
