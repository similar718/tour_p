package cn.xmzt.www.selfdrivingtools.bean;

import com.amap.api.navi.model.AMapNaviPath;

public class SharedNavigationGoHereBeanBack {

    private AMapNaviPath item;
    private boolean is_checked;
    private String data;

    public SharedNavigationGoHereBeanBack(AMapNaviPath item, boolean is_checked) {
        this.item = item;
        this.is_checked = is_checked;
    }

    public AMapNaviPath getItem() {
        return item;
    }

    public void setItem(AMapNaviPath item) {
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
