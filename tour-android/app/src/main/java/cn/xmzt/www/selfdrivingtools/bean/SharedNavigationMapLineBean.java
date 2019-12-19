package cn.xmzt.www.selfdrivingtools.bean;

import com.amap.api.maps.model.LatLng;
import cn.xmzt.www.selfdrivingtools.overlay.DrivingRouteOverlay;
import cn.xmzt.www.selfdrivingtools.overlay.SharedHomeDrivingRouteOverlay;

public class SharedNavigationMapLineBean {
    LatLng beans ;
    String title ;
    String content;
    String url;
    int type; // 0 景点 1 住宿
    DrivingRouteOverlay drivingRouteOverlay;

    SharedHomeDrivingRouteOverlay homeDrivingRouteOverlay;

    public SharedHomeDrivingRouteOverlay getHomeDrivingRouteOverlay() {
        return homeDrivingRouteOverlay;
    }

    public void setHomeDrivingRouteOverlay(SharedHomeDrivingRouteOverlay homeDrivingRouteOverlay) {
        this.homeDrivingRouteOverlay = homeDrivingRouteOverlay;
    }

    public SharedNavigationMapLineBean(LatLng beans, String title, String content, int type) {
        this.beans = beans;
        this.title = title;
        this.type = type;
        this.content = content;
    }

    public SharedNavigationMapLineBean(LatLng beans, String title, String content, String url, int type) {
        this.beans = beans;
        this.title = title;
        this.content = content;
        this.url = url;
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public DrivingRouteOverlay getDrivingRouteOverlay() {
        return drivingRouteOverlay;
    }

    public void setDrivingRouteOverlay(DrivingRouteOverlay drivingRouteOverlay) {
        this.drivingRouteOverlay = drivingRouteOverlay;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LatLng getBeans() {
        return beans;
    }

    public void setBeans(LatLng beans) {
        this.beans = beans;
    }

}
