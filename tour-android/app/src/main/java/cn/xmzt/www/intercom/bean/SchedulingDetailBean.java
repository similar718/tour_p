package cn.xmzt.www.intercom.bean;

import android.text.TextUtils;

import java.util.List;

public class SchedulingDetailBean {

    /**
     * lineId : 1
     * departDate : 2019-10-30
     * endDate : 2019-11-02
     * state : 1
     * lineName : 成都集合3日稻城-西藏自驾游-中秋佳节
     * photoUrl : http://s1.lvjs.com.cn/uploads/pc/place2/2015-02-16/04943ae2-3112-4fbe-9b37-adef49a31401_480_320.jpg
     * departArea : {"areaCode":"440300","parentCode":"440000","treeNames":"广东省/深圳市","areaName":"深圳市","areaType":2}
     * arrivalArea : {"areaCode":"422801","parentCode":"422800","treeNames":"湖北省/恩施土家族苗族自治州/恩施市","areaName":"恩施市","areaType":3}
     * viaInfo : 迪士尼乐园，著名景点XX，未知景点XX
     * lineRouteList : [{"id":4,"lineId":1,"title":"全国-成都","dayNum":1,"date":"2019-10-30","staging":{"areaCode":"510100","parentCode":"510000","treeNames":"四川省/成都市","areaName":"成都市","areaType":2},"stagingCoor":{"longitude":104.081143,"latitude":30.650767},"start":{"areaCode":"510100","parentCode":"510000","treeNames":"四川省/成都市","areaName":"成都市","areaType":2},"startCoor":{"longitude":104.081143,"latitude":30.650767},"end":{"areaCode":"510100","parentCode":"510000","treeNames":"四川省/成都市","areaName":"成都市","areaType":2},"endCoor":{"longitude":104.081143,"latitude":30.650767},"content":"赠送一晚天府之国-成都酒店住宿。（由全国川藏自驾联盟统一安排、不住不退）领队通知开行前说明会，也可以队友集体AA制，品成都地道火锅。","accommodation":"成都准四星酒店","accommodationCoor":{"longitude":116.407526,"latitude":39.90403},"breakfast":"自理","lunch":"自理","dinner":"自理","car":"全程高速，路况良好","carXy":"","spotsList":[{"title":"迪士尼乐园","content":"亲爱的游客朋友,当听到滔滔的流水声时,您就来到了著名的黄果树大瀑布。贵州省镇宁县水河上的昔里树瀑布形成约有5万年号区内分布若雄","images":["https://pics.lvjs.com.cn/uploads/pc/place2/2019-04-07/8df04b1b-ad91-40cf-97ef-15611b21b1fc.jpg","https://pf.xmzt.cn/img/20190824/5e5926fd30a8410f8eb760aca924ba99.png"],"coordinate":{"longitude":114.057868,"latitude":22.543099},"address":"深圳"}]},{"id":5,"lineId":1,"title":"成都-雅江","dayNum":2,"date":"2019-10-31","staging":{"areaCode":"510100","parentCode":"510000","treeNames":"四川省/成都市","areaName":"成都市","areaType":2},"stagingCoor":{"longitude":104.146301,"latitude":30.632772},"start":{"areaCode":"510100","parentCode":"510000","treeNames":"四川省/成都市","areaName":"成都市","areaType":2},"startCoor":{"longitude":104.066541,"latitude":30.572269},"end":{"areaCode":"513325","parentCode":"513300","treeNames":"四川省/甘孜藏族自治州/雅江县","areaName":"雅江县","areaType":3},"endCoor":{"longitude":101.014425,"latitude":30.031533},"content":"成都出发经成雅高速公路抵达雅安：【雨城雅安】，以\u201c雅雨、雅女、雅鱼\u201d三绝而闻名于世。抵达泸定桥后游览并用午餐，之后到达摄影家天堂-新都桥镇或雅江入住酒店。","accommodation":"雅江酒店","accommodationCoor":{"longitude":116.407526,"latitude":39.90403},"breakfast":"自理","lunch":"自理","dinner":"自理","car":"全程高速，路况良好","carXy":"","spotsList":[{"title":"著名景点XX","content":"ss","images":["https://pics.lvjs.com.cn/uploads/pc/place2/2019-04-07/e56aec97-d0c0-4e17-95a0-8e0d3e559987.jpg"],"coordinate":{"longitude":22.543099,"latitude":114.057868},"address":"深圳"}]},{"id":6,"lineId":1,"title":"雅江-亚丁","dayNum":3,"date":"2019-11-01","staging":{"areaCode":"513325","parentCode":"513300","treeNames":"四川省/甘孜藏族自治州/雅江县","areaName":"雅江县","areaType":3},"stagingCoor":{"longitude":101.01258,"latitude":30.031793},"start":{"areaCode":"513325","parentCode":"513300","treeNames":"四川省/甘孜藏族自治州/雅江县","areaName":"雅江县","areaType":3},"startCoor":{"longitude":101.014425,"latitude":30.031533},"end":{"areaCode":"110107","parentCode":"110100","treeNames":"北京市/北京城区/石景山区","areaName":"石景山区","areaType":3},"endCoor":{"longitude":116.222982,"latitude":39.906611},"content":"早起尽赏晨曦中景色如画的小桥、流水、人家、藏式民居风光。 途中将经过雅江，然后登上剪子弯山（4658米）、翻卡子拉山（海拔4718 米），最后到达第一高城--理塘（海拔4014米），后前往香格里拉镇入住酒店。","accommodation":"亚丁酒店","accommodationCoor":{"longitude":116.416357,"latitude":39.928353},"breakfast":"自理","lunch":"自理","dinner":"自理","car":"全程高速，路况良好","carXy":"","spotsList":[{"title":"未知景点XX","content":"地方","images":["https://pics.lvjs.com.cn/uploads/pc/place2/2019-04-07/cd9669fa-6b4e-42f9-bb12-a62eb0898344.jpg","https://pics.lvjs.com.cn/uploads/pc/place2/2019-04-07/ca1e1696-fb9f-41c0-8338-aaa4c63f6f98.jpg"],"coordinate":{"longitude":22.543099,"latitude":114.057868},"address":"深圳"}]}]
     */

    private int lineId;
    private String departDate;
    private String endDate;
    private int state;
    private String lineName;
    private String photoUrl;
    private DepartAreaBean departArea;
    private ArrivalAreaBean arrivalArea;
    private String viaInfo;
    private List<LineRouteListBean> lineRouteList;

    public int getLineId() {
        return lineId;
    }

    public void setLineId(int lineId) {
        this.lineId = lineId;
    }

    public String getDepartDate() {
        return departDate;
    }

    public void setDepartDate(String departDate) {
        this.departDate = departDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public DepartAreaBean getDepartArea() {
        return departArea;
    }

    public void setDepartArea(DepartAreaBean departArea) {
        this.departArea = departArea;
    }

    public ArrivalAreaBean getArrivalArea() {
        return arrivalArea;
    }

    public void setArrivalArea(ArrivalAreaBean arrivalArea) {
        this.arrivalArea = arrivalArea;
    }

    public String getViaInfo() {
        return viaInfo;
    }

    public void setViaInfo(String viaInfo) {
        this.viaInfo = viaInfo;
    }

    public List<LineRouteListBean> getLineRouteList() {
        return lineRouteList;
    }

    public void setLineRouteList(List<LineRouteListBean> lineRouteList) {
        this.lineRouteList = lineRouteList;
    }

    public static class DepartAreaBean {
        /**
         * areaCode : 440300
         * parentCode : 440000
         * treeNames : 广东省/深圳市
         * areaName : 深圳市
         * areaType : 2
         */

        private String areaCode;
        private String parentCode;
        private String treeNames;
        private String areaName;
        private int areaType;

        public String getAreaCode() {
            return areaCode;
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
        }

        public String getParentCode() {
            return parentCode;
        }

        public void setParentCode(String parentCode) {
            this.parentCode = parentCode;
        }

        public String getTreeNames() {
            return treeNames;
        }

        public void setTreeNames(String treeNames) {
            this.treeNames = treeNames;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public int getAreaType() {
            return areaType;
        }

        public void setAreaType(int areaType) {
            this.areaType = areaType;
        }
    }

    public static class ArrivalAreaBean {
        /**
         * areaCode : 422801
         * parentCode : 422800
         * treeNames : 湖北省/恩施土家族苗族自治州/恩施市
         * areaName : 恩施市
         * areaType : 3
         */

        private String areaCode;
        private String parentCode;
        private String treeNames;
        private String areaName;
        private int areaType;

        public String getAreaCode() {
            return areaCode;
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
        }

        public String getParentCode() {
            return parentCode;
        }

        public void setParentCode(String parentCode) {
            this.parentCode = parentCode;
        }

        public String getTreeNames() {
            return treeNames;
        }

        public void setTreeNames(String treeNames) {
            this.treeNames = treeNames;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public int getAreaType() {
            return areaType;
        }

        public void setAreaType(int areaType) {
            this.areaType = areaType;
        }
    }

    public static class LineRouteListBean {
        /**
         * id : 4
         * lineId : 1
         * title : 全国-成都
         * dayNum : 1
         * date : 2019-10-30
         * staging : {"areaCode":"510100","parentCode":"510000","treeNames":"四川省/成都市","areaName":"成都市","areaType":2}
         * stagingCoor : {"longitude":104.081143,"latitude":30.650767}
         * start : {"areaCode":"510100","parentCode":"510000","treeNames":"四川省/成都市","areaName":"成都市","areaType":2}
         * startCoor : {"longitude":104.081143,"latitude":30.650767}
         * end : {"areaCode":"510100","parentCode":"510000","treeNames":"四川省/成都市","areaName":"成都市","areaType":2}
         * endCoor : {"longitude":104.081143,"latitude":30.650767}
         * content : 赠送一晚天府之国-成都酒店住宿。（由全国川藏自驾联盟统一安排、不住不退）领队通知开行前说明会，也可以队友集体AA制，品成都地道火锅。
         * accommodation : 成都准四星酒店
         * accommodationCoor : {"longitude":116.407526,"latitude":39.90403}
         * breakfast : 自理
         * lunch : 自理
         * dinner : 自理
         * car : 全程高速，路况良好
         * carXy :
         * spotsList : [{"title":"迪士尼乐园","content":"亲爱的游客朋友,当听到滔滔的流水声时,您就来到了著名的黄果树大瀑布。贵州省镇宁县水河上的昔里树瀑布形成约有5万年号区内分布若雄","images":["https://pics.lvjs.com.cn/uploads/pc/place2/2019-04-07/8df04b1b-ad91-40cf-97ef-15611b21b1fc.jpg","https://pf.xmzt.cn/img/20190824/5e5926fd30a8410f8eb760aca924ba99.png"],"coordinate":{"longitude":114.057868,"latitude":22.543099},"address":"深圳"}]
         */

        private int id;
        private int lineId;
        private String title;
        private int dayNum;
        private String date;
        private StagingBean staging; //集结点
        private StagingCoorBean stagingCoor;
        private StartBean start;//起点
        private StartCoorBean startCoor;//开始坐标
        private EndBean end;//结束点
        private EndCoorBean endCoor;//结束坐标
        private String content;//行程内容
        private String accommodation;//酒店
        private AccommodationCoorBean accommodationCoor;//酒店坐标
        private String breakfast;//早餐
        private String lunch;//午餐
        private String dinner;//晚餐
        private String car;//交通
        private String carXy;
        private List<SpotsListBean> spotsList;//景区列表

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getLineId() {
            return lineId;
        }

        public void setLineId(int lineId) {
            this.lineId = lineId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getDayNum() {
            return dayNum;
        }

        public void setDayNum(int dayNum) {
            this.dayNum = dayNum;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public StagingBean getStaging() {
            return staging;
        }

        public void setStaging(StagingBean staging) {
            this.staging = staging;
        }

        public StagingCoorBean getStagingCoor() {
            return stagingCoor;
        }

        public void setStagingCoor(StagingCoorBean stagingCoor) {
            this.stagingCoor = stagingCoor;
        }

        public StartBean getStart() {
            return start;
        }

        public void setStart(StartBean start) {
            this.start = start;
        }

        public StartCoorBean getStartCoor() {
            return startCoor;
        }

        public void setStartCoor(StartCoorBean startCoor) {
            this.startCoor = startCoor;
        }

        public EndBean getEnd() {
            return end;
        }

        public void setEnd(EndBean end) {
            this.end = end;
        }

        public EndCoorBean getEndCoor() {
            return endCoor;
        }

        public void setEndCoor(EndCoorBean endCoor) {
            this.endCoor = endCoor;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAccommodation() {
            return accommodation;
        }

        public void setAccommodation(String accommodation) {
            this.accommodation = accommodation;
        }

        public AccommodationCoorBean getAccommodationCoor() {
            return accommodationCoor;
        }

        public void setAccommodationCoor(AccommodationCoorBean accommodationCoor) {
            this.accommodationCoor = accommodationCoor;
        }

        /**
         * 是否有进餐时间
         * @return
         */
        public boolean isMealTime(){
            if(!TextUtils.isEmpty(breakfast)||!TextUtils.isEmpty(lunch)||!TextUtils.isEmpty(dinner)){
                return true;
            }
            return false;
        }
        public String getBreakfast() {
            return breakfast;
        }

        public void setBreakfast(String breakfast) {
            this.breakfast = breakfast;
        }

        public String getLunch() {
            return lunch;
        }

        public void setLunch(String lunch) {
            this.lunch = lunch;
        }

        public String getDinner() {
            return dinner;
        }

        public void setDinner(String dinner) {
            this.dinner = dinner;
        }

        public String getCar() {
            return car;
        }

        public void setCar(String car) {
            this.car = car;
        }

        public String getCarXy() {
            return carXy;
        }

        public void setCarXy(String carXy) {
            this.carXy = carXy;
        }

        public List<SpotsListBean> getSpotsList() {
            return spotsList;
        }

        public void setSpotsList(List<SpotsListBean> spotsList) {
            this.spotsList = spotsList;
        }

        public static class StagingBean {
            /**
             * areaCode : 510100
             * parentCode : 510000
             * treeNames : 四川省/成都市
             * areaName : 成都市
             * areaType : 2
             */

            private String areaCode;
            private String parentCode;
            private String treeNames;
            private String areaName;
            private int areaType;

            public String getAreaCode() {
                return areaCode;
            }

            public void setAreaCode(String areaCode) {
                this.areaCode = areaCode;
            }

            public String getParentCode() {
                return parentCode;
            }

            public void setParentCode(String parentCode) {
                this.parentCode = parentCode;
            }

            public String getTreeNames() {
                return treeNames;
            }

            public void setTreeNames(String treeNames) {
                this.treeNames = treeNames;
            }

            public String getAreaName() {
                return areaName;
            }

            public void setAreaName(String areaName) {
                this.areaName = areaName;
            }

            public int getAreaType() {
                return areaType;
            }

            public void setAreaType(int areaType) {
                this.areaType = areaType;
            }
        }

        public static class StagingCoorBean {
            /**
             * longitude : 104.081143
             * latitude : 30.650767
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

        public static class StartBean {
            /**
             * areaCode : 510100
             * parentCode : 510000
             * treeNames : 四川省/成都市
             * areaName : 成都市
             * areaType : 2
             */

            private String areaCode;
            private String parentCode;
            private String treeNames;
            private String areaName;
            private int areaType;

            public String getAreaCode() {
                return areaCode;
            }

            public void setAreaCode(String areaCode) {
                this.areaCode = areaCode;
            }

            public String getParentCode() {
                return parentCode;
            }

            public void setParentCode(String parentCode) {
                this.parentCode = parentCode;
            }

            public String getTreeNames() {
                return treeNames;
            }

            public void setTreeNames(String treeNames) {
                this.treeNames = treeNames;
            }

            public String getAreaName() {
                return areaName;
            }

            public void setAreaName(String areaName) {
                this.areaName = areaName;
            }

            public int getAreaType() {
                return areaType;
            }

            public void setAreaType(int areaType) {
                this.areaType = areaType;
            }
        }

        public static class StartCoorBean {
            /**
             * longitude : 104.081143
             * latitude : 30.650767
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

        public static class EndBean {
            /**
             * areaCode : 510100
             * parentCode : 510000
             * treeNames : 四川省/成都市
             * areaName : 成都市
             * areaType : 2
             */

            private String areaCode;
            private String parentCode;
            private String treeNames;
            private String areaName;
            private int areaType;

            public String getAreaCode() {
                return areaCode;
            }

            public void setAreaCode(String areaCode) {
                this.areaCode = areaCode;
            }

            public String getParentCode() {
                return parentCode;
            }

            public void setParentCode(String parentCode) {
                this.parentCode = parentCode;
            }

            public String getTreeNames() {
                return treeNames;
            }

            public void setTreeNames(String treeNames) {
                this.treeNames = treeNames;
            }

            public String getAreaName() {
                return areaName;
            }

            public void setAreaName(String areaName) {
                this.areaName = areaName;
            }

            public int getAreaType() {
                return areaType;
            }

            public void setAreaType(int areaType) {
                this.areaType = areaType;
            }
        }

        public static class EndCoorBean {
            /**
             * longitude : 104.081143
             * latitude : 30.650767
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

        public static class AccommodationCoorBean {
            /**
             * longitude : 116.407526
             * latitude : 39.90403
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

        public static class SpotsListBean {
            /**
             * title : 迪士尼乐园
             * content : 亲爱的游客朋友,当听到滔滔的流水声时,您就来到了著名的黄果树大瀑布。贵州省镇宁县水河上的昔里树瀑布形成约有5万年号区内分布若雄
             * images : ["https://pics.lvjs.com.cn/uploads/pc/place2/2019-04-07/8df04b1b-ad91-40cf-97ef-15611b21b1fc.jpg","https://pf.xmzt.cn/img/20190824/5e5926fd30a8410f8eb760aca924ba99.png"]
             * coordinate : {"longitude":114.057868,"latitude":22.543099}
             * address : 深圳
             */

            private String title;
            private String content;
            private CoordinateBean coordinate;
            private String address;
            private List<String> images;

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

            public CoordinateBean getCoordinate() {
                return coordinate;
            }

            public void setCoordinate(CoordinateBean coordinate) {
                this.coordinate = coordinate;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public List<String> getImages() {
                return images;
            }

            public void setImages(List<String> images) {
                this.images = images;
            }

            public static class CoordinateBean {
                /**
                 * longitude : 114.057868
                 * latitude : 22.543099
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
        }
    }
}
