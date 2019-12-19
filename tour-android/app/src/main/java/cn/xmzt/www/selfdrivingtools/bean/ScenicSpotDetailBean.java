package cn.xmzt.www.selfdrivingtools.bean;

import java.util.List;

public class ScenicSpotDetailBean {

    /**
     * id : 233045
     * name : 赤石丹心
     * description : 大自然是一个充满魅力的地方，到处是神奇之处，不说一些世界未解之谜，......
     * photoUrl : https://map.365daoyou.cn/cm/fileOpviewFile?fileName=mimi-001_1557801216859-g8uj9f2f.jpg
     * lonLat : {"longitude":113.870542,"latitude":22.586484}
     * mapZoomLevel : 18
     * distance : 0.0949307307600975
     * distanceFormat : 0.1m
     * atlasLists : [{"url":"https://map.365daoyou.cn/cm/fileOpviewFile?fileName=mimi-001_1557801216859-g8uj9f2f.jpg"}]
     * scenicSpotList : [{"id":233046,"name":"琴之韵景区","description":"亲爱的游客朋友，您现在来到的是琴之韵景区，其实在这个景点中，并不是......","photoUrl":"https://map.365daoyou.cn/cm/fileOpviewFile?fileName=mimi-001_1557801215895-08kognpo.jpg","lonLat":{"longitude":113.869866,"latitude":22.587346},"mapZoomLevel":18,"distance":118.33236489348243,"distanceFormat":"118.3m","scenicResource":{"id":6962,"resUrl":"https://pf.xmzt.cn/scenic/audio/14908/233046.mp3","resType":1}},{"id":233044,"name":"翠环凝碧","description":"如果说林之声景区是注重于树林的声音，那么翠环凝碧就是注重树林颜色的......","photoUrl":"https://map.365daoyou.cn/cm/fileOpviewFile?fileName=mimi-001_1557801210045-1jj87xj0.jpg","lonLat":{"longitude":113.871668,"latitude":22.586158},"mapZoomLevel":18,"distance":121.14682570831607,"distanceFormat":"121.1m","scenicResource":{"id":6964,"resUrl":"https://pf.xmzt.cn/scenic/audio/14908/233044.mp3","resType":1}},{"id":233047,"name":"南竹琴台","description":"在武侠小说中经常会出现这样的场景，某个武艺高超的大侠，或者是不问世......","photoUrl":"https://map.365daoyou.cn/cm/fileOpviewFile?fileName=mimi-001_1557801211933-lny8e9t9.jpg","lonLat":{"longitude":113.869812,"latitude":22.58801},"mapZoomLevel":18,"distance":185.4887450114181,"distanceFormat":"185.5m","scenicResource":{"id":6961,"resUrl":"https://pf.xmzt.cn/scenic/audio/14908/233047.mp3","resType":1}}]
     */

    private int id;
    private String name;
    private String description;
    private String photoUrl;
    private LonLatBean lonLat;
    private int mapZoomLevel;
    private double distance;
    private String distanceFormat;
    private List<AtlasListsBean> atlasLists;
    private List<ScenicSpotListBean> scenicSpotList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public LonLatBean getLonLat() {
        return lonLat;
    }

    public void setLonLat(LonLatBean lonLat) {
        this.lonLat = lonLat;
    }

    public int getMapZoomLevel() {
        return mapZoomLevel;
    }

    public void setMapZoomLevel(int mapZoomLevel) {
        this.mapZoomLevel = mapZoomLevel;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getDistanceFormat() {
        return distanceFormat;
    }

    public void setDistanceFormat(String distanceFormat) {
        this.distanceFormat = distanceFormat;
    }

    public List<AtlasListsBean> getAtlasLists() {
        return atlasLists;
    }

    public void setAtlasLists(List<AtlasListsBean> atlasLists) {
        this.atlasLists = atlasLists;
    }

    public List<ScenicSpotListBean> getScenicSpotList() {
        return scenicSpotList;
    }

    public void setScenicSpotList(List<ScenicSpotListBean> scenicSpotList) {
        this.scenicSpotList = scenicSpotList;
    }

    public static class LonLatBean {
        /**
         * longitude : 113.870542
         * latitude : 22.586484
         */

        private double longitude;
        private double latitude;

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }
    }

    public static class AtlasListsBean {
        /**
         * url : https://map.365daoyou.cn/cm/fileOpviewFile?fileName=mimi-001_1557801216859-g8uj9f2f.jpg
         */

        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class ScenicSpotListBean {
        /**
         * id : 233046
         * name : 琴之韵景区
         * description : 亲爱的游客朋友，您现在来到的是琴之韵景区，其实在这个景点中，并不是......
         * photoUrl : https://map.365daoyou.cn/cm/fileOpviewFile?fileName=mimi-001_1557801215895-08kognpo.jpg
         * lonLat : {"longitude":113.869866,"latitude":22.587346}
         * mapZoomLevel : 18
         * distance : 118.33236489348243
         * distanceFormat : 118.3m
         * scenicResource : {"id":6962,"resUrl":"https://pf.xmzt.cn/scenic/audio/14908/233046.mp3","resType":1}
         */

        private int id;
        private String name;
        private String description;
        private String photoUrl;
        private LonLatBeanX lonLat;
        private int mapZoomLevel;
        private double distance;
        private String distanceFormat;
        private ScenicResourceBean scenicResource;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPhotoUrl() {
            return photoUrl;
        }

        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }

        public LonLatBeanX getLonLat() {
            return lonLat;
        }

        public void setLonLat(LonLatBeanX lonLat) {
            this.lonLat = lonLat;
        }

        public int getMapZoomLevel() {
            return mapZoomLevel;
        }

        public void setMapZoomLevel(int mapZoomLevel) {
            this.mapZoomLevel = mapZoomLevel;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public String getDistanceFormat() {
            return distanceFormat;
        }

        public void setDistanceFormat(String distanceFormat) {
            this.distanceFormat = distanceFormat;
        }

        public ScenicResourceBean getScenicResource() {
            return scenicResource;
        }

        public void setScenicResource(ScenicResourceBean scenicResource) {
            this.scenicResource = scenicResource;
        }

        public static class LonLatBeanX {
            /**
             * longitude : 113.869866
             * latitude : 22.587346
             */

            private double longitude;
            private double latitude;

            public double getLongitude() {
                return longitude;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }

            public double getLatitude() {
                return latitude;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }
        }

        public static class ScenicResourceBean {
            /**
             * id : 6962
             * resUrl : https://pf.xmzt.cn/scenic/audio/14908/233046.mp3
             * resType : 1
             */

            private int id;
            private String resUrl;
            private int resType;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getResUrl() {
                return resUrl;
            }

            public void setResUrl(String resUrl) {
                this.resUrl = resUrl;
            }

            public int getResType() {
                return resType;
            }

            public void setResType(int resType) {
                this.resType = resType;
            }
        }
    }
}
