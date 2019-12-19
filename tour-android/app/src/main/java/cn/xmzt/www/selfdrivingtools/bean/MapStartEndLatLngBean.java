package cn.xmzt.www.selfdrivingtools.bean;

import com.amap.api.services.core.LatLonPoint;

public class MapStartEndLatLngBean {

    LatLonPoint start;
    LatLonPoint end;

    public MapStartEndLatLngBean(LatLonPoint start, LatLonPoint end) {
        this.start = start;
        this.end = end;
    }

    public LatLonPoint getStart() {
        return start;
    }

    public void setStart(LatLonPoint start) {
        this.start = start;
    }

    public LatLonPoint getEnd() {
        return end;
    }

    public void setEnd(LatLonPoint end) {
        this.end = end;
    }
}
