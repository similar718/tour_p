package cn.xmzt.www.route.bean;

/**
 * 主题
 */

public class FilterThemeBean {
    private String code;//主题编码
    private String label;//主题标签
    private boolean isSelected;//主题是否选中

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
