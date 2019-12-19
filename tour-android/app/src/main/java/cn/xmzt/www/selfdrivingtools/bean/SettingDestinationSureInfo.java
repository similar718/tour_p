package cn.xmzt.www.selfdrivingtools.bean;

public class SettingDestinationSureInfo {
    String titleName;
    double lat;
    double lng;

    public SettingDestinationSureInfo(String titleName, double lat, double lng) {
        this.titleName = titleName;
        this.lat = lat;
        this.lng = lng;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
