package cn.xmzt.www.route.bean;

/**
 * 日期选择
 */

public class DaySelectBean {
    private String day;//天数
    private boolean isSelect;//是否选择

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
