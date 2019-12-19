package cn.xmzt.www.selfdrivingtools.bean;

import com.amap.api.services.core.PoiItem;

public class SettingDestinationBottomBean {

    private PoiItem item;
    private boolean is_checked;
    private String data;

    public SettingDestinationBottomBean(PoiItem item, boolean is_checked) {
        this.item = item;
        this.is_checked = is_checked;
    }

    public SettingDestinationBottomBean(PoiItem item, boolean is_checked,String data) {
        this.item = item;
        this.is_checked = is_checked;
        this.data = data;
    }

    public PoiItem getItem() {
        return item;
    }

    public void setItem(PoiItem item) {
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
