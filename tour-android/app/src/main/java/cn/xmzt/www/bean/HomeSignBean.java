package cn.xmzt.www.bean;

import java.io.Serializable;

/**
 * @author tanlei
 * @date 2019/9/18
 * @describe
 */

public class HomeSignBean implements Serializable {
    private boolean isSign;
    private String name;
    private String value;

    public HomeSignBean() {
    }

    public HomeSignBean(boolean isSign, String name, String value) {
        this.isSign = isSign;
        this.name = name;
        this.value = value;
    }

    public boolean isSign() {
        return isSign;
    }

    public void setSign(boolean sign) {
        isSign = sign;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "HomeSignBean{" +
                "isSign=" + isSign +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
