package cn.xmzt.www.bean;

import java.io.Serializable;

/**
 * 实体的基类
 */
public class BaseBean implements Serializable {
    private String reCode;//响应码
    private String reMsg;//响应描述
    public String getReCode() {
        return reCode;
    }

    public void setReCode(String reCode) {
        this.reCode = reCode;
    }

    public String getReMsg() {
        return reMsg;
    }

    public void setReMsg(String reMsg) {
        this.reMsg = reMsg;
    }
}
