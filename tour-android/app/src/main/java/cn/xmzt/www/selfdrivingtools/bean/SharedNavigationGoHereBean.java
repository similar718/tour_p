package cn.xmzt.www.selfdrivingtools.bean;

import com.amap.api.services.route.DrivePath;

public class SharedNavigationGoHereBean {

    private DrivePath item;
    private boolean is_checked;
    private String data;

    public SharedNavigationGoHereBean(DrivePath item, boolean is_checked) {
        this.item = item;
        this.is_checked = is_checked;
    }

    public DrivePath getItem() {
        return item;
    }

    public void setItem(DrivePath item) {
        this.item = item;
    }

    public boolean isIs_checked() {
        return is_checked;
    }

    public void setIs_checked(boolean is_checked) {
        this.is_checked = is_checked;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
