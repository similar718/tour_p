package cn.xmzt.www.route.bean;

/**
 * 主题
 */

public class ThemeBean {

    private String theme;//主题

    private boolean isSelect;//是否选择

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
