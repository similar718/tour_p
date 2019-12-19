package cn.xmzt.www.route.bean;

import com.amap.api.maps.model.LatLng;
import cn.xmzt.www.bean.BaseBean;

import java.util.List;

/**
 * 线路预览信息
 */
public class RoutePreviewBean extends BaseBean {
    private int lineId; //线路id
    private String lineName;//线路名称
    private String tripDistance;//总行程 如100km
    private int days;//天数
    private int nights;//晚上数
    private List<LatLng> coordinateList;//经纬度坐标
    private List<ScenicSpotIntro> scenicSpotIntroList;//景点介绍列表

    public int getLineId() {
        return lineId;
    }

    public void setLineId(int lineId) {
        this.lineId = lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getTripDistance() {
        if(tripDistance==null){
            return "";
        }
        return tripDistance;
    }

    public void setTripDistance(String tripDistance) {
        this.tripDistance = tripDistance;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getNights() {
        return nights;
    }

    public void setNights(int nights) {
        this.nights = nights;
    }

    public List<LatLng> getCoordinateList() {
        return coordinateList;
    }

    public void setCoordinateList(List<LatLng> coordinateList) {
        this.coordinateList = coordinateList;
    }

    public List<ScenicSpotIntro> getScenicSpotIntroList() {
        return scenicSpotIntroList;
    }

    public void setScenicSpotIntroList(List<ScenicSpotIntro> scenicSpotIntroList) {
        this.scenicSpotIntroList = scenicSpotIntroList;
    }

    /**
     * 行程途径景点数
     * @return
     */
    public int getScenicSpotCount() {
        if(scenicSpotIntroList==null){
            return 0;
        }
        return scenicSpotIntroList.size();
    }
    /**
     * 经纬度坐标
     */
    public static class Coordinate {
        private double longitude;
        private double latitude;
        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
        public double getLongitude() {
            return longitude;
        }
        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }
        public double getLatitude() {
            return latitude;
        }
    }

    /**
     * 景点介绍
     */
    public class ScenicSpotIntro {

        private String title;
        private String content;
        private List<String> images;
        private LatLng coordinate;
        public void setTitle(String title) {
            this.title = title;
        }
        public String getTitle() {
            if(title==null){
                return "";
            }
            return title;
        }

        public void setContent(String content) {
            this.content = content;
        }
        public String getContent() {
            return content;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
        public List<String> getImages() {
            return images;
        }

        public void setCoordinate(LatLng coordinate) {
            this.coordinate = coordinate;
        }
        public LatLng getCoordinate() {
            return coordinate;
        }

    }
}
