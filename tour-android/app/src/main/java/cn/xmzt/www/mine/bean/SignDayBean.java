package cn.xmzt.www.mine.bean;

/**
 * @author tanlei
 * @date 2019/8/24
 * @describe
 */

public class SignDayBean {
    private String day;
    private boolean isSign;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public boolean isSign() {
        return isSign;
    }

    public void setSign(boolean sign) {
        isSign = sign;
    }

    public SignDayBean() {
    }

    public SignDayBean(String day) {
        this.day = day;
    }

}
